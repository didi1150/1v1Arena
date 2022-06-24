package me.didi.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.utilities.ChatUtils;

/**
 * @author Dezhong Zhuang
 * 
 *         Getter for all player stats <br>
 *         Singleton class
 */
public class CurrentStatGetter {

	private static CurrentStatGetter instance;
	private CustomPlayerManager customPlayerManager;

	public CurrentStatGetter(CustomPlayerManager customPlayerManager) {
		this.customPlayerManager = customPlayerManager;
	}

	public static void init(CustomPlayerManager customPlayerManager) {
		instance = new CurrentStatGetter(customPlayerManager);
	}

	public static CurrentStatGetter getInstance() {
		return instance;
	}

	/**
	 * Returns base health + bonusHealth
	 */
	public int getMaxHealth(Player player) {
		CustomPlayer customPlayer = customPlayerManager.getPlayer(player.getUniqueId());

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return 0;
		}

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

		return (int) (bonusHealth + customPlayer.getBaseHealth());
	}

	public int getCurrentArmor(Player player) {

		CustomPlayer customPlayer = customPlayerManager.getPlayer(player.getUniqueId());

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return 0;
		}

		int bonusDefense = 0;
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

		return (int) (bonusDefense + customPlayer.getBaseDefense());

	}

	public int getCurrentMagicResistance(Player player) {

		CustomPlayer customPlayer = customPlayerManager.getPlayer(player.getUniqueId());

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return 0;
		}

		float bonusMagicResistance = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("magic resistance:")) {
						String health = ChatColor.stripColor(string.split(": ")[1]);
						bonusMagicResistance += Integer.parseInt(health);
					}
				}
			}
		}
		return (int) (bonusMagicResistance + customPlayer.getMagicResist());
	}

	public int getArmorPenetration(Player player) {
		CustomPlayer customPlayer = customPlayerManager.getPlayer(player.getUniqueId());

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return 0;
		}

		float bonusArmorPenetration = 0;
		for (ItemStack itemStack : player.getInventory().getContents()) {
			if (itemStack == null || itemStack.getType() == Material.AIR)
				continue;
			if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
				// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
				for (String string : itemStack.getItemMeta().getLore()) {
					if (string.contains("armor penetration:")) {
						String health = ChatColor.stripColor(string.split(": ")[1]);
						bonusArmorPenetration += Integer.parseInt(health);
					}
				}
			}
		}
		return (int) (bonusArmorPenetration + customPlayer.getArmorPenetration());
	}

	public int getMagicPenetration(Player player) {
		CustomPlayer customPlayer = customPlayerManager.getPlayer(player.getUniqueId());
		float bonusMagicPenetration = 0;
		if ((customPlayer = getCustomPlayer(player)) != null) {

			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("magic penetration:")) {
							String health = ChatColor.stripColor(string.split(": ")[1]);
							bonusMagicPenetration += Integer.parseInt(health);
						}
					}
				}
			}
			return (int) (bonusMagicPenetration + customPlayer.getMagicPenetration());
		}
		return 0;
	}

	private CustomPlayer getCustomPlayer(Player player) {
		CustomPlayer customPlayer = customPlayerManager.getPlayer(player.getUniqueId());

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return null;
		}

		return customPlayer;
	}

}
