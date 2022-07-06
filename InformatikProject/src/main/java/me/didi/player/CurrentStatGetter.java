package me.didi.player;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.didi.items.CustomItemManager;
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
	private CustomItemManager customItemManager;

	public CurrentStatGetter(CustomItemManager customItemManager) {
		this.customItemManager = customItemManager;
	}

	public static void init(CustomItemManager customItemManager) {
		instance = new CurrentStatGetter(customItemManager);
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
			final AtomicInteger bonusHealth = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return (int) customPlayer.getBaseHealth();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				if (customItem.getBaseStats().getBaseHealth() > 0)
					bonusHealth.addAndGet(customItem.getBaseStats().getBaseHealth());
			});

			return (int) (bonusHealth.get() + customPlayer.getBaseHealth());
		}
		return 0;
	}

	public int getCurrentArmor(Player player) {

		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusDefense = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return (int) customPlayer.getBaseDefense();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				if (customItem.getBaseStats().getBaseDefense() > 0)
					bonusDefense.addAndGet(customItem.getBaseStats().getBaseDefense());
			});

			return (int) (bonusDefense.get() + customPlayer.getBaseDefense());
		}
		return 0;
	}

	public int getCurrentMagicResistance(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusMagicResistance = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return (int) customPlayer.getMagicResist();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				int baseMagicResist = customItem.getBaseStats().getBaseMagicResist();
				if (baseMagicResist > 0)
					bonusMagicResistance.addAndGet(baseMagicResist);
			});

			return (int) (bonusMagicResistance.get() + customPlayer.getMagicResist());
		}
		return 0;
	}

	public int getArmorPenetration(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusArmorPenetration = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return (int) customPlayer.getArmorPenetration();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				int baseArmorPenetration = customItem.getBaseStats().getBaseArmorPenetration();
				if (baseArmorPenetration > 0)
					bonusArmorPenetration.addAndGet(baseArmorPenetration);
			});

			return (int) (bonusArmorPenetration.get() + customPlayer.getArmorPenetration());
		}
		return 0;
	}

	public int getMagicPenetration(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusMagicPenetration = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return (int) customPlayer.getMagicPenetration();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				int baseMagicPenetration = customItem.getBaseStats().getBaseMagicPenetration();
				if (baseMagicPenetration > 0)
					bonusMagicPenetration.addAndGet(baseMagicPenetration);
			});

			return (int) (bonusMagicPenetration.get() + customPlayer.getMagicPenetration());
		}
		return 0;
	}

	public double getAttackDamage(Player player) {
		if (player == null)
			return 0;

		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusAttackDamage = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return customPlayer.getAttackDamage();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				int baseAttackDamage = customItem.getBaseStats().getBaseAttackDamage();
				if (baseAttackDamage > 0)
					bonusAttackDamage.addAndGet(baseAttackDamage);
			});

			return (bonusAttackDamage.get() + customPlayer.getAttackDamage());
		}
		return 0;
	}

	public double getAbilityPower(Player player) {
		if (player == null)
			return 0;
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusAbilityPower = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return customPlayer.getAbilityPower();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				int baseAbilityPower = customItem.getBaseStats().getBaseAbilityPower();
				if (baseAbilityPower > 0)
					bonusAbilityPower.addAndGet(baseAbilityPower);
			});

			return (bonusAbilityPower.get() + customPlayer.getAbilityPower());
		}
		return 0;
	}

	public float getAttackSpeed(Player player) {
		CustomPlayer customPlayer;
		if ((customPlayer = getCustomPlayer(player)) != null) {
			final AtomicInteger bonusAttackSpeed = new AtomicInteger(0);
			if (!customItemManager.getSelectedItems().containsKey(player))
				return customPlayer.getBaseAttackSpeed();
			customItemManager.getSelectedItems().get(player).forEach(customItem -> {
				int baseAttackSpeed = customItem.getBaseStats().getAttackSpeed();
				if (baseAttackSpeed > 0)
					bonusAttackSpeed.addAndGet(baseAttackSpeed);
			});

			return ((float) bonusAttackSpeed.get() / 100 + customPlayer.getBaseAttackSpeed());
		}
		return 0;
	}

	public CustomPlayer getCustomPlayer(Player player) {
		CustomPlayer customPlayer = CustomPlayerManager.getInstance().getPlayer(player);

		if (customPlayer == null) {
			ChatUtils.sendDebugMessage(
					ChatColor.RED + "No custom player profile found: " + ChatColor.YELLOW + player.getName());
			return null;
		}

		return customPlayer;
	}

}
