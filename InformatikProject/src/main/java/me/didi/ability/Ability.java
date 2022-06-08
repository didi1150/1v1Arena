package me.didi.ability;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public interface Ability {

	String getName();

	ItemStack getIcon();

	List<String> getDescription();

	AbilityType getAbilityType();

	int getCooldown();

}