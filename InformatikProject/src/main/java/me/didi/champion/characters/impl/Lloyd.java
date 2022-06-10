package me.didi.champion.characters.impl;

import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.MeleeChampion;

public class Lloyd extends MeleeChampion {

	public Lloyd(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {
		// TODO
	}

	@Override
	public void executeFirstAbility() {
		getAbilities()[0].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeSecondAbility() {

	}

	@Override
	public void executeThirdAbility() {

	}

	@Override
	public void executeUltimate() {
		getAbilities()[3].execute(abilityStateManager, player, specialEffectsManager);
	}
}
