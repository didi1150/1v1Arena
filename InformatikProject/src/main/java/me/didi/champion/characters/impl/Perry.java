package me.didi.champion.characters.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.MeleeChampion;

public class Perry extends MeleeChampion {


	public Perry(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			int baseAttackDamage, int baseAbilityPower, int baseArmorPenetration, int baseMagicPenetration,
			float attackSpeed, ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, baseAttackDamage, baseAbilityPower,
				baseArmorPenetration, baseMagicPenetration, attackSpeed, icon, autoAttackItem);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeAutoAttack(Player player) {

	}
}
