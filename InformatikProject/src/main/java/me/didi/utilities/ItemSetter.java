package me.didi.utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemSetter {

	public void setItem(Player player, int index, ItemStack newItem) {
		player.getInventory().setItem(index, newItem);
		player.updateInventory();
	}
	
	

}
