package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class COSMIC_DRIVE extends CustomItem {

	public COSMIC_DRIVE(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(250, 0, 0, 0, 0, 0, 90, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.IRON_HELMET).setDisplayName(ChatColor.GOLD + "Cosmic Drive").setLore(getLore())
				.toItemStack();
	}

	@Override
	public CustomItem clone() {
		return new COSMIC_DRIVE(itemPassives);
	}
}
