package me.didi.player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.champion.Champion;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.events.customEvents.HealReason;
import me.didi.player.effects.SpecialEffect;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;
import me.didi.utilities.TaskManager;

public class CustomPlayerManager {

	private static Map<UUID, CustomPlayer> players = new HashMap<>();
	private static Map<CustomPlayer, SpecialEffect> nextOnHitEffect = new HashMap<>();

	private static BukkitTask bukkitTask;
	private static MainClass plugin;

	private static AbilityStateManager abilityStateManager;
	private static int counter = 0;

	private static CustomPlayerManager instance;

	public static void init(MainClass plugin, AbilityStateManager abilityStateManager) {
		instance = new CustomPlayerManager();
		CustomPlayerManager.plugin = plugin;
		CustomPlayerManager.abilityStateManager = abilityStateManager;
	}

	public static CustomPlayerManager getInstance() {
		return instance;
	}

	public void addPlayer(Player player, Champion champion) {

		UUID uuid = player.getUniqueId();
		String name = player.getName();

		players.put(uuid,
				new CustomPlayer(champion.getBaseHealth(), champion.getBaseDefense(), champion.getBaseMagicResist(),
						champion.getBaseArmorPenetration(), champion.getBaseMagicPenetration(),
						champion.getBaseAttackDamage(), champion.getBaseAbilityPower(), champion.getBaseAttackSpeed(),
						uuid, name));

	}

	public void removePlayer(UUID uuid) {
		players.remove(uuid);
	}

	public CustomPlayer getPlayer(UUID uuid) {
		if (players.containsKey(uuid)) {
			for (Map.Entry<UUID, CustomPlayer> entry : players.entrySet()) {
				if (entry.getKey() == uuid) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	public void addSpecialEffect(UUID uuid, SpecialEffect specialEffect) {
		nextOnHitEffect.put(getPlayer(uuid), specialEffect);
	}

	public void removeSpecialEffect(UUID uuid) {
		nextOnHitEffect.remove(getPlayer(uuid));
	}

	public void startBackgroundTask() {

		bukkitTask = TaskManager.getInstance().repeat(1, 1, task -> {
			if (counter >= 20) {
				players.keySet().forEach(uuid -> {
					regenHealth(getPlayer(uuid));
				});

				counter = 0;
			}

			players.keySet().forEach(uuid -> {
				Bukkit.getPlayer(uuid).getWorld().setTime(0);
				Bukkit.getPlayer(uuid).getWorld().setStorm(false);
				CustomPlayer customPlayer = getPlayer(uuid);
				setHealth(customPlayer);
				setShield(customPlayer);
				sendInfos(customPlayer);
			});

			counter++;
		});
	}

	public void stopBackgroundTask() {
		if (bukkitTask == null)
			return;
		bukkitTask.cancel();
	}

	private void regenHealth(CustomPlayer customPlayer) {
		float regenAmount = 1.75f;
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		float maxHealth = customPlayer.getBaseHealth() + getBonusHealth(player);

		if (customPlayer.getCurrentHealth() + regenAmount > maxHealth)
			regenAmount = maxHealth - customPlayer.getCurrentHealth();

		Bukkit.getPluginManager()
				.callEvent(new CustomPlayerHealEvent(customPlayer, HealReason.REGENERATION, regenAmount));
	}

	private void setShield(CustomPlayer customPlayer) {
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		float maxHealth = customPlayer.getBaseHealth() + getBonusHealth(player);

		double factor = maxHealth / player.getMaxHealth();

		double shield = factor * customPlayer.getRemainingShield();

		((CraftPlayer) player).getHandle().setAbsorptionHearts((float) shield);
	}

	private void setHealth(CustomPlayer customPlayer) {
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		float maxHealth = customPlayer.getBaseHealth() + getBonusHealth(player);

		if (customPlayer.getCurrentHealth() / maxHealth * player.getMaxHealth() <= player.getMaxHealth())
			player.setHealth(customPlayer.getCurrentHealth() / maxHealth * player.getMaxHealth());
		else
			player.setHealth(player.getMaxHealth());
	}

	private void sendInfos(CustomPlayer customPlayer) {

		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		int maxHealth = (int) (customPlayer.getBaseHealth() + getBonusHealth(player));

		if (customPlayer.getRemainingShield() == 0) {

			ChatUtils.sendActionBar(player, ChatColor.GREEN
					+ new DecimalFormat("#").format((customPlayer.getBaseDefense() + getBonusDefense(player))) + "✜"
					+ ChatColor.RED + " " + new DecimalFormat("#").format(customPlayer.getCurrentHealth()) + "/"
					+ maxHealth + "❤" + ChatColor.AQUA + " "
					+ new DecimalFormat("#").format((customPlayer.getMagicResist() + getBonusMagicResistance(player)))
					+ "⦾");
		} else {
			ChatUtils.sendActionBar(player, ChatColor.GREEN
					+ new DecimalFormat("#").format((customPlayer.getBaseDefense() + getBonusDefense(player))) + "✜"
					+ ChatColor.YELLOW + " "
					+ new DecimalFormat("#").format(customPlayer.getCurrentHealth() + customPlayer.getRemainingShield())
					+ "/" + maxHealth + "❤" + ChatColor.AQUA + " "
					+ new DecimalFormat("#").format((customPlayer.getMagicResist() + getBonusMagicResistance(player)))
					+ "⦾");
		}
	}

	public int getBonusHealth(Player player) {
		if (player == null)
			return 0;
		int bonusHealth = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + health: ChatColor.GREEN + 40
				if (itemStack.getItemMeta().getLore().contains("health")) {
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("health")) {
							String health = ChatColor.stripColor(string.split(": ")[1]);
							bonusHealth += Integer.parseInt(health);
						}
					}
				}
			}
		}
		return bonusHealth;
	}

	public float getBonusDefense(Player player) {
		if (player == null)
			return 0;
		float bonusDefense = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + health: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("defense:")) {
						String health = ChatColor.stripColor(string.split(": ")[1]);
						bonusDefense += Integer.parseInt(health);
					}
				}
			}
		}
		return bonusDefense;
	}

	public float getBonusMagicResistance(Player player) {
		if (player == null)
			return 0;
		float bonusMagicResistance = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + health: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("magic resistance:")) {
						String health = ChatColor.stripColor(string.split(": ")[1]);
						bonusMagicResistance += Integer.parseInt(health);
					}
				}
			}
		}
		return bonusMagicResistance;
	}

	public void setGhost(Player player) {
		if (players.containsKey(player.getUniqueId()))
			removePlayer(player.getUniqueId());
		if (plugin.getAlivePlayers().contains(player.getUniqueId()))
			plugin.getAlivePlayers().remove(player.getUniqueId());

		abilityStateManager.removeCooldowns(player);

		player.setAllowFlight(true);
		player.setFlying(true);

		player.getInventory().clear();
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
		player.getInventory().setHelmet(ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_GHOST));
	}

}
