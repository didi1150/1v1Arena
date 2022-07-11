package me.didi.items.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class WITS_END extends CustomItem {

	public WITS_END(List<ItemPassive> itemPassives) {
		super(itemPassives);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 40, 0, 0, 40, 0, 40);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(ChatColor.GOLD + "Wit's End").setLore(getLore())
				.toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new WITS_END(itemPassives);
	}
}
