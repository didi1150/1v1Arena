package me.didi.ability;

import org.bukkit.inventory.ItemStack;

public abstract class Ability {

	/**
	 * Name of the Ability
	 */
	private String name;

	/**
	 * Icon used as representation
	 */
	private ItemStack icon;

	public Ability(String name, ItemStack icon) {
		this.name = name;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public ItemStack getIcon() {
		return icon;
	}

	/**
	 * Casts the spell of a champion
	 */
	public abstract void cast();

	/**
	 * Stops the spell of a champion
	 */
	public abstract void cancel();

}
