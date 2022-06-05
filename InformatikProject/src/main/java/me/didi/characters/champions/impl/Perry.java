package me.didi.characters.champions.impl;

import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.MeleeChampion;

public class Perry extends MeleeChampion {

	public Perry(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public Champion clone() {
		return new Perry(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon(),
				getAutoAttackItem());
	}

	@Override
	public void executeAutoAttack() {
		// TODO Auto-generated method stub

	}

	private void shootBoomerang() {
		
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

	@Override
	public void stopAllTasks() {

	}

}
