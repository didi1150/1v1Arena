package me.didi.champion.characters;

import org.bukkit.inventory.ItemStack;

import me.didi.champion.Champion;
import me.didi.champion.ability.Ability;

public abstract class RangedChampion extends Champion {

	public RangedChampion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			int baseAttackDamage, int baseAbilityPower, int baseArmorPenetration, int baseMagicPenetration,
			ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, baseAttackDamage, baseAbilityPower,
				baseArmorPenetration, baseMagicPenetration, icon, autoAttackItem);
	}


}
