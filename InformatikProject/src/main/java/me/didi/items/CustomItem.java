package me.didi.items;

import org.bukkit.inventory.ItemStack;

import me.didi.utilities.BaseStats;

public abstract class CustomItem implements ItemPassive {

	protected BaseStats baseStats;

	protected ItemStack itemStack;

	public CustomItem(BaseStats baseStats) {
		this.baseStats = baseStats;
	}

	public BaseStats getBaseStats() {
		return baseStats;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
