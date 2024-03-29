package me.didi.items.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.items.passives.LIFELINE;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class STERAKS_GAGE extends CustomItem {

	public STERAKS_GAGE(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(400, 0, 0, 0, 0, 40, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.GOLD_BARDING).setDisplayName(ChatColor.GOLD + "Sterak's Gage")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new STERAKS_GAGE(itemPassives);
	}
}
