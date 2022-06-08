package me.didi.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class AbilityImpl implements Ability {

	/**
	 * Name of the Ability
	 */
	private String name;

	private List<String> description;

	private AbilityType abilityType;
	/**
	 * Icon used as representation
	 */
	private ItemStack icon;

	private int cooldown;

	public AbilityImpl(AbilityType abilityType, String name, ItemStack icon, int cooldown, String... description) {
		this.name = name;
		this.icon = icon;
		this.description = Arrays.asList(description);
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
	public List<String> getDescription() {
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
}