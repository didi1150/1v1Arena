package me.didi.items.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.items.passives.SPELLDANCE;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class COSMIC_DRIVE extends CustomItem {

	@Override
	public BaseStats getBaseStats() {
		// TODO Auto-generated method stub
		return new BaseStats(250, 0, 0, 0, 0, 0, 90, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.IRON_HELMET).setDisplayName(ChatColor.GOLD + "Cosmic Drive").setLore(getLore())
				.toItemStack();
	}

	@Override
	public List<ItemPassive> getItemPassives() {
		return Arrays.asList(new SPELLDANCE());
	}

}
