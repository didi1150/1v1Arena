package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class LORD_DOMINIKS_REGARDS extends CustomItem {

	public LORD_DOMINIKS_REGARDS(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 0, 30, 0, 55, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.IRON_SWORD).setDisplayName(ChatColor.GOLD + "Lord Dominik's Regards")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new LORD_DOMINIKS_REGARDS(itemPassives);
	}
}