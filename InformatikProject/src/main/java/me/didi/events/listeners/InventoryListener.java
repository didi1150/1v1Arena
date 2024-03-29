package me.didi.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import me.didi.menus.Menu;

public class InventoryListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(true);
		InventoryHolder holder = event.getInventory().getHolder();
		if (holder instanceof Menu) {
			if (event.getCurrentItem() == null) {
				return;
			}
			Menu menu = (Menu) holder;
			menu.handleMenu(event);
		}

	}

}
