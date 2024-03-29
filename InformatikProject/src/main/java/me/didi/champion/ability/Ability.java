package me.didi.champion.ability;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.player.effects.SpecialEffectsManager;

public interface Ability {

	String getName();

	ItemStack getIcon();

	String[] getDescription();

	AbilityType getAbilityType();

	int getCooldown();

	void execute(AbilityStateManager abilityStateManager, Player player, SpecialEffectsManager specialEffectsManager);

}