package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class ESSENCE_REAVER extends CustomItem {

	public ESSENCE_REAVER(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 0, 0, 0, 80, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.DIAMOND_AXE).setDisplayName(ChatColor.GOLD + "Essence Reaver")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new ESSENCE_REAVER(itemPassives);
	}
}
