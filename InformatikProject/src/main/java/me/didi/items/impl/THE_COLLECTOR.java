package me.didi.items.impl;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;

public class THE_COLLECTOR extends CustomItem {

	public THE_COLLECTOR(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		return null;
	}

	@Override
	public ItemStack getItemStack() {
		return null;
	}

	@Override
	public CustomItem clone() {
		return new THE_COLLECTOR(itemPassives);
	}

}
