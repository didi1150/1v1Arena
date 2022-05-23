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

	/**
	 * Icon used as representation
	 */
	private ItemStack icon;

	public Ability(String name, ItemStack icon, String...description) {
		this.name = name;
		this.icon = icon;
		this.description = Arrays.asList(description);
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
}
