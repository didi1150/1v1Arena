package me.didi.items;

import org.bukkit.inventory.ItemStack;

import me.didi.utilities.BaseStats;

public interface CustomItem extends ItemPassive {

	public BaseStats getBaseStats();

	public ItemStack getItemStack();

}
