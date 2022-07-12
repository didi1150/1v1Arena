package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class THE_COLLECTOR extends CustomItem {

	public THE_COLLECTOR(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 0, 15, 0, 75, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.FEATHER).setDisplayName(ChatColor.GOLD + "The Collector").setLore(getLore())
				.toItemStack();
	}

	@Override
	public CustomItem clone() {
		return new THE_COLLECTOR(itemPassives);
	}

}
