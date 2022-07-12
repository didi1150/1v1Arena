package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class SERYLDAS_GRUDGE extends CustomItem {

	public SERYLDAS_GRUDGE(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 0, 30, 0, 45, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.IRON_SPADE).setDisplayName(ChatColor.GOLD + "Serylda's Grudge")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public CustomItem clone() {
		return new SERYLDAS_GRUDGE(itemPassives);
	}
}
