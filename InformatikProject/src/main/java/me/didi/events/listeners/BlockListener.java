package me.didi.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
		event.getPlayer().updateInventory();
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}

}
