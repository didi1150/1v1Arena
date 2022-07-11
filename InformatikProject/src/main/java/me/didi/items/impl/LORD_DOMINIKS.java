package me.didi.items.impl;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;

public class LORD_DOMINIKS extends CustomItem {

	public LORD_DOMINIKS(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getItemStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new LORD_DOMINIKS(itemPassives);
	}
}