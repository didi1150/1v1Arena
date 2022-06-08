package me.didi.champion.characters;

import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.ability.AbilityImpl;
import me.didi.champion.Champion;

public abstract class MeleeChampion extends Champion {

	public MeleeChampion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}
}
