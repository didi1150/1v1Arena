package me.didi.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.utilities.ChatUtils;

/**
 * Getter for all player stats <br>
 * Singleton class
 * 
 * @author Dezhong Zhuang
 * 
 * 
 */
public class CurrentStatGetter {

	private static CurrentStatGetter instance;

	public static void init() {
		instance = new CurrentStatGetter();
	}

	public static CurrentStatGetter getInstance() {
		return instance;
	}

	/**
	 * Returns base health + bonusHealth
	 */
	public int getMaxHealth(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			int bonusHealth = 0;

			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + health: ChatColor.GREEN + 40
					if (itemStack.getItemMeta().getLore().contains("health")) {
						for (String string : itemStack.getItemMeta().getLore()) {
							if (string.contains("health")) {
								String health = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
								bonusHealth += Integer.parseInt(health);
							}
						}
					}
				}
			}

			return (int) (bonusHealth + customPlayer.getBaseHealth());
		}
		return 0;
	}

	public int getCurrentArmor(Player player) {

		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			int bonusDefense = 0;
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + health: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("defense:")) {
							String def = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
							bonusDefense += Integer.parseInt(def);
						}
					}
				}
			}

			return (int) (bonusDefense + customPlayer.getBaseDefense());
		}
		return 0;
	}

	public int getCurrentMagicResistance(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {

			float bonusMagicResistance = 0;
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("magic resistance:")) {
							String mr = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
							bonusMagicResistance += Integer.parseInt(mr);
						}
					}
				}
			}
			return (int) (bonusMagicResistance + customPlayer.getMagicResist());
		}
		return 0;
	}

	public int getArmorPenetration(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			float bonusArmorPenetration = 0;
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("armor penetration:")) {
							String am = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
							bonusArmorPenetration += Integer.parseInt(am);
						}
					}
				}
			}
			return (int) (bonusArmorPenetration + customPlayer.getArmorPenetration());
		}
		return 0;
	}

	public int getMagicPenetration(Player player) {
		CustomPlayer customPlayer;
		float bonusMagicPenetration = 0;
		if ((customPlayer = getCustomPlayer(player)) != null) {

			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("magic penetration:")) {
							String mp = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
							bonusMagicPenetration += Integer.parseInt(mp);
						}
					}
				}
			}
			return (int) (bonusMagicPenetration + customPlayer.getMagicPenetration());
		}
		return 0;
	}

	public double getAttackDamage(Player player) {
		if (player == null)
			return 0;

		CustomPlayer customPlayer = null;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			double damage = 0;
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + health: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("attack damage:")) {
							String toAdd = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
							damage += Integer.parseInt(toAdd);
						}
					}
				}
			}
			return damage + customPlayer.getAttackDamage();
		}
		return 0;
	}

	public double getAbilityPower(Player player) {
		if (player == null)
			return 0;

		CustomPlayer customPlayer = null;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			double abilityPower = 0;
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + health: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("ability power:")) {
							String toAdd = ChatColor.stripColor(string.split(": ")[1].replaceAll("\\+", ""));
							abilityPower += Integer.parseInt(toAdd);
						}
					}
				}
			}
			return abilityPower + customPlayer.getAbilityPower();
		}
		return 0;
	}

	public float getAttackSpeed(Player player) {
		float bonusAttackSpeed = 0;

		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {

			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack == null || itemStack.getType() == Material.AIR)
					continue;
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().getLore() != null) {
					// ChatColor.Red + magic resistance: ChatColor.GREEN + 40
					for (String string : itemStack.getItemMeta().getLore()) {
						if (string.contains("attack speed:")) {
							String as = ChatColor.stripColor(string.split(": ")[1].replaceAll("[%\\+]", ""));
							bonusAttackSpeed += Float.parseFloat(as) / 100;
						}
					}
				}
			}
			return bonusAttackSpeed + customPlayer.getBaseAttackSpeed();
		}
		return 0;
	}

	public CustomPlayer getCustomPlayer(Player player) {
		CustomPlayer customPlayer = CustomPlayerManager.getInstance().getPlayer(player.getUniqueId());

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return null;
		}

		return customPlayer;
	}

}
