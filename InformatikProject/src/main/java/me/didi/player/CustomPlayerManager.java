package me.didi.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.player.effects.SpecialEffect;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;

public class CustomPlayerManager {

	private Map<UUID, CustomPlayer> players = new HashMap<>();
	private Map<CustomPlayer, SpecialEffect> nextOnHitEffect = new HashMap<>();

	private BukkitTask bukkitTask;
	private MainClass plugin;

	public CustomPlayerManager(MainClass plugin) {
		this.plugin = plugin;
	}

	public void addPlayer(Player player) {

		UUID uuid = player.getUniqueId();
		String name = player.getName();

		players.put(uuid, new CustomPlayer(100, 0, 0, 0, 0, uuid, name));

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
		bukkitTask = new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {

				if (counter >= 20 * 2) {
					plugin.getAlivePlayers().forEach(uuid -> {
						regenHealth(getPlayer(uuid));
					});

					counter = 0;
				}
				plugin.getAlivePlayers().forEach(uuid -> {
					CustomPlayer customPlayer = getPlayer(uuid);
					setHealth(customPlayer);
					sendHealthBar(customPlayer);
				});

				counter++;
			}
		}.runTaskTimer(plugin, 1, 1);
	}

	public void stopBackgroundTask() {
		if (bukkitTask == null)
			return;
		bukkitTask.cancel();
	}

	private void regenHealth(CustomPlayer customPlayer) {
		int regenAmount = 2;
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		float maxHealth = customPlayer.getBaseHealth() + getBonusHealth(player);

		if (customPlayer.getCurrentHealth() + regenAmount > maxHealth) {
			customPlayer.setCurrentHealth(maxHealth);
		} else {
			customPlayer.setCurrentHealth(customPlayer.getCurrentHealth() + regenAmount);
		}
	}

	private void setHealth(CustomPlayer customPlayer) {
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		float maxHealth = customPlayer.getBaseHealth() + getBonusHealth(player);
		player.setHealth(customPlayer.getCurrentHealth() / maxHealth * player.getMaxHealth());
	}

	private void sendHealthBar(CustomPlayer customPlayer) {
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		int maxHealth = (int) (customPlayer.getBaseHealth() + getBonusHealth(player));
		ChatUtils.sendActionBar(player, ChatColor.RED + "" + customPlayer.getCurrentHealth() + "/" + maxHealth + "❤");
	}

	public int getBonusHealth(Player player) {
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
		float bonusDefense = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + health: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("defense")) {
						String health = ChatColor.stripColor(string.split(": ")[1]);
						bonusDefense += Integer.parseInt(health);
					}
				}
			}
		}
		return bonusDefense;
	}

	public float getBonusMagicResistance(Player player) {
		float bonusMagicResistance = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + health: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("magic resistance")) {
						String health = ChatColor.stripColor(string.split(": ")[1]);
						bonusMagicResistance += Integer.parseInt(health);
					}
				}
			}
		}
		return bonusMagicResistance;
	}

	public double getDamage(Player player) {
		double damage = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + health: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("damage")) {
						String toAdd = ChatColor.stripColor(string.split(": ")[1]);
						damage += Integer.parseInt(toAdd);
					}
				}
			}
		}
		return damage;
	}

	public void setGhost(Player player) {
		if (players.containsKey(player.getUniqueId()))
			removePlayer(player.getUniqueId());
		if (plugin.getAlivePlayers().contains(player.getUniqueId()))
			plugin.getAlivePlayers().remove(player.getUniqueId());

		player.setAllowFlight(true);
		player.setFlying(true);

		player.getInventory().clear();
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
		player.getInventory().setHelmet(ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_GHOST.getTitle(),
				SkullFactory.HEAD_GHOST.getName()));
	}

}
