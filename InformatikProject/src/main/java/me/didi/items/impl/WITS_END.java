package me.didi.items.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.utilities.BaseStats;

public class WITS_END implements CustomItem {

	
	
	@Override
	public void runPassive(Event event, Player player) {
		
	}

	@Override
	public BaseStats getBaseStats() {
		return new BaseStats(0, 0, 40, 0, 0, 40, 0, 40);
	}

	@Override
	public ItemStack getItemStack() {
		return null;
	}

}
