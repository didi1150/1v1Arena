package me.didi.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.player.effects.SpecialEffect;
import me.didi.utilities.ChatUtils;

public class CustomPlayerManager {

	private Map<UUID, CustomPlayer> players = new HashMap<>();
	private Map<CustomPlayer, SpecialEffect> nextOnHitEffect = new HashMap<>();

	private BukkitTask bukkitTask;

	public void addPlayer(Player player) {

		UUID uuid = player.getUniqueId();
		String name = player.getName();

		players.put(uuid, new CustomPlayer(100, 100, 0, 0, 0, uuid, name));

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

			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(player -> {
					regenHealth(getPlayer(player.getUniqueId()));
				});
			}
		}.runTaskTimer(MainClass.getPlugin(), 20, 20);
	}

	public void stopBackgroundTask() {
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
		sendHealthBar(customPlayer);
		player.setHealth(customPlayer.getCurrentHealth() / maxHealth * player.getMaxHealth());
	}

	private void sendHealthBar(CustomPlayer customPlayer) {
		Player player = Bukkit.getPlayer(customPlayer.getUuid());
		int maxHealth = (int) (customPlayer.getBaseHealth() + getBonusHealth(player));
		ChatUtils.sendActionBar(player, ChatColor.RED + "" + (int) customPlayer.getCurrentHealth() + "/" + maxHealth + "❤");
	}

	private int getBonusHealth(Player player) {
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

}
