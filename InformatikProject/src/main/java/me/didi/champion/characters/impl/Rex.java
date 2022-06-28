package me.didi.champion.characters.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.RangedChampion;
import me.didi.player.CurrentStatGetter;
import me.didi.utilities.Utils;

public class Rex extends RangedChampion {

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			int baseAttackDamage, int baseAbilityPower, int baseArmorPenetration, int baseMagicPenetration,
			float attackSpeed, ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, baseAttackDamage, baseAbilityPower,
				baseArmorPenetration, baseMagicPenetration, attackSpeed, icon, autoAttackItem);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeAutoAttack() {
		Utils.shootAutoAttackProjectile(player, 13, new ItemStack(Material.PRISMARINE_CRYSTALS),
				CurrentStatGetter.getInstance().getAttackDamage(player), true, 0.75);
	}
}
