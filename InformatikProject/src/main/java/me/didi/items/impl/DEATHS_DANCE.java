package me.didi.items.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.items.passives.IGNORE_PAIN;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class DEATHS_DANCE extends CustomItem {

	public DEATHS_DANCE(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 45, 0, 0, 0, 55, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.WOOD_SPADE).setDisplayName(ChatColor.GOLD + "Death's Dance").setLore(getLore())
				.toItemStack();
	}

}
