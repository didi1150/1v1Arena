package me.didi.characters.champions;

import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.Champion;

public abstract class RangedChampion extends Champion {

	public RangedChampion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}
}
