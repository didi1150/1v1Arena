package me.didi.ability;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Ability {
	
	String getName();

	ItemStack getIcon();

	String[] getDescription();

	AbilityType getAbilityType();

	int getCooldown();

	void execute(AbilityStateManager abilityStateManager, Player player);
	
}