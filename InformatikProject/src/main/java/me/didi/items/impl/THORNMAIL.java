package me.didi.items.impl;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.items.passives.THORNS;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class THORNMAIL extends CustomItem {

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(350, 50, 0, 0, 0, 0, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(ChatColor.GOLD + "Thornmail").setLore(getLore())
				.toItemStack();
	}

	@Override
	public List<ItemPassive> getItemPassives() {
		// TODO Auto-generated method stub
		return Arrays.asList(new THORNS());
	}

}
