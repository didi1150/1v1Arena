package me.didi.items.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class WITS_END extends CustomItem {

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 40, 0, 0, 40, 0, 40);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(ChatColor.GOLD + "Wit's End")
				.setLore(getLore()).toItemStack();
	}

}
