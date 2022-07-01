package me.didi.items.impl;

import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.utilities.BaseStats;

public class STERAKS_GAGE extends CustomItem {

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(400, 0, 0, 0, 0, 40, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return null;
	}

}
