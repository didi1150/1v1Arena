package me.didi.champion.characters.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.RangedChampion;
import me.didi.utilities.MathUtils;

public class Rex extends RangedChampion {

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {
		MathUtils.shootAutoAttackProjectile(player, 13, new ItemStack(Material.PRISMARINE_CRYSTALS),
				customPlayerManager.getDamage(player), true, 0.75);
	}
}
