package me.didi.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemListener implements Listener {

	@EventHandler
	public void dropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

}
