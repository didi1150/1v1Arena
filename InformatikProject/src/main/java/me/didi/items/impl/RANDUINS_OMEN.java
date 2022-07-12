package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class RANDUINS_OMEN extends CustomItem {

	public RANDUINS_OMEN(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(250, 90, 0, 0, 0, 0, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(ChatColor.GOLD + "Randuin's Omen")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new RANDUINS_OMEN(itemPassives);
	}

}
