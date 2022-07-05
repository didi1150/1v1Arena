package me.didi.items.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.items.passives.SPELLBLADE;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class ESSENCE_REAVER extends CustomItem {

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 0, 0, 0, 80, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.DIAMOND_BARDING).setDisplayName(ChatColor.GOLD + "Essence Reaver")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public List<ItemPassive> getItemPassives() {
		return Arrays.asList(new SPELLBLADE());
	}

}
