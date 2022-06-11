package me.didi.champion.ability.impl.anakin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.player.effects.SpecialEffectsManager;

public class AnakinThirdAbility implements Ability {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public ItemStack getIcon() {
		return null;
	}

	@Override
	public String[] getDescription() {
		return null;
	}

	@Override
	public AbilityType getAbilityType() {
		return null;
	}

	@Override
	public int getCooldown() {
		return 0;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {

	}

}
