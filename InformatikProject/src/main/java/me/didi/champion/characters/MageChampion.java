package me.didi.champion.characters;

import org.bukkit.inventory.ItemStack;

import me.didi.champion.Champion;
import me.didi.champion.ability.Ability;

public abstract class MageChampion extends Champion {

	public MageChampion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}
}
