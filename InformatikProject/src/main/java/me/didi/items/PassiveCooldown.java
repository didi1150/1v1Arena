package me.didi.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PassiveCooldown {

	private Player player;
	private int slot;
	private int remainingCooldown;
	private ItemStack cachedItem;

	public PassiveCooldown(Player player, int slot, int cooldown, ItemStack cachedItem) {
		this.player = player;
		this.slot = slot;
		this.remainingCooldown = cooldown;
		this.cachedItem = cachedItem;
	}

	public Player getPlayer() {
		return player;
	}

	public int getSlot() {
		return slot;
	}

	public int getRemainingCooldown() {
		return remainingCooldown;
	}

	public ItemStack getCachedItem() {
		return cachedItem;
	}

	public void setRemainingCooldown(int cooldown) {
		this.remainingCooldown = cooldown;
	}
	
}
