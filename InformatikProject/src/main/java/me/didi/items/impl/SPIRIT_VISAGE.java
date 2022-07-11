package me.didi.items.impl;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.items.ItemPassive;
import me.didi.utilities.BaseStats;
import me.didi.utilities.ItemBuilder;

public class SPIRIT_VISAGE extends CustomItem {

	public SPIRIT_VISAGE(List<ItemPassive> itemPassives) {
		super(itemPassives);
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(450, 0, 50, 0, 0, 0, 0, 0);
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setDisplayName(ChatColor.GOLD + "Spirit Visage")
				.setLore(getLore()).toItemStack();
	}

	@Override
	public CustomItem clone() {
		// TODO Auto-generated method stub
		return new SPIRIT_VISAGE(itemPassives);
	}
}
