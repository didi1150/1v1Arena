package me.didi.characters.champions;

import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.Champion;

public abstract class MeleeChampion extends Champion {

	public MeleeChampion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon);
	}


}
