package me.didi.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.didi.utilities.BaseStats;

public abstract class CustomItem implements ItemPassive {

	public abstract BaseStats getBaseStats();

	public abstract ItemStack getItemStack();

	
	//TODO
	public List<String> addStatsToLore() {
		List<String> lore = new ArrayList<String>();
		BaseStats baseStats = getBaseStats();

		baseStats.getAttackSpeed();
		baseStats.getBaseAbilityPower();
		baseStats.getBaseArmorPenetration();
		baseStats.getBaseAttackDamage();
		baseStats.getBaseDefense();
		baseStats.getBaseHealth();
		baseStats.getBaseMagicPenetration();
		baseStats.getBaseMagicResist();
		
		return lore;
	}

}
