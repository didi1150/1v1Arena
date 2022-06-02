package me.didi.ability;

import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Ability {

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

	public Ability(AbilityType abilityType, String name, ItemStack icon, int cooldown, String... description) {
		this.name = name;
		this.icon = icon;
		this.description = Arrays.asList(description);
		this.cooldown = cooldown;
	}

	public String getName() {
		return name;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public List<String> getDescription() {
		return description;
	}

	public AbilityType getAbilityType() {
		return abilityType;
	}

	public int getCooldown() {
		return cooldown;
	}
}
