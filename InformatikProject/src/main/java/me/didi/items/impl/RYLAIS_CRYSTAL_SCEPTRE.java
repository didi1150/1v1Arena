package me.didi.items.impl;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;

public class RYLAIS_CRYSTAL_SCEPTRE extends CustomItem {

	public RYLAIS_CRYSTAL_SCEPTRE(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
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
		return new RYLAIS_CRYSTAL_SCEPTRE(itemPassives);
	}
}
