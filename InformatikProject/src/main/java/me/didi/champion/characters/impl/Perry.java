package me.didi.champion.characters.impl;

import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.MeleeChampion;

public class Perry extends MeleeChampion {

	public Perry(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {

	}

	@Override
	public void executeFirstAbility() {
	}

	@Override
	public void executeSecondAbility() {

	}

	@Override
	public void executeThirdAbility() {

	}

	@Override
	public void executeUltimate() {

	}
}
