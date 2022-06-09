package me.didi.champion.ability;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityImpl implements Ability {

	/**
	 * Name of the Ability
	 */
	private String name;

	private String[] description;

	private AbilityType abilityType;
	/**
	 * Icon used as representation
	 */
	private ItemStack icon;

	private int cooldown;

	public AbilityImpl(AbilityType abilityType, String name, ItemStack icon, int cooldown, String... description) {
		this.name = name;
		this.icon = icon;
		this.description = description;
		this.cooldown = cooldown;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getIcon() {
		return icon;
	}

	@Override
	public String[] getDescription() {
		return description;
	}

	@Override
	public AbilityType getAbilityType() {
		return abilityType;
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player) {
		// TODO Auto-generated method stub

	}
}