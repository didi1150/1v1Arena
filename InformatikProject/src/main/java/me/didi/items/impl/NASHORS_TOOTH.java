package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class NASHORS_TOOTH extends CustomItem {

	public NASHORS_TOOTH(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseStats getBaseStats() {
		// TODO Auto-generated method stub
		return new BaseStats(0, 0, 0, 0, 0, 0, 100, 50);
	}

	@Override
	public ItemStack getItemStack() {
		// TODO Auto-generated method stub
		return new ItemBuilder(Material.GOLD_SWORD).setDisplayName(ChatColor.GOLD + "Nashor's Tooth").setLore(getLore())
				.toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new NASHORS_TOOTH(itemPassives);
	}
}
