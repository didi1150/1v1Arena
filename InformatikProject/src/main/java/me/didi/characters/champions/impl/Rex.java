package me.didi.characters.champions.impl;

import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.RangedChampion;

public class Rex extends RangedChampion {

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Champion clone() {
		// TODO Auto-generated method stub
		return new Rex(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon());
	}

	@Override
	public void executeAutoAttack() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeFirstAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeSecondAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeThirdAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeUltimate() {
		// TODO Auto-generated method stub

	}

}
