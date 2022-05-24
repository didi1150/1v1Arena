package me.didi.characters.champions.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.champions.MeleeChampion;

public class Anakin extends MeleeChampion{

	public Anakin(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon);
	}

	@Override
	public void executeAutoAttack() {
	}

	@Override
	public void executeFirstAbility(Player player) {
		
	}

	@Override
	public void executeSecondAbility(Player player) {
		
	}

	@Override
	public void executeThirdAbility(Player player) {
		
	}

	@Override
	public void executeUltimate(Player player) {
		
	}

}
