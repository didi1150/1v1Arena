package me.didi.items;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomItemManager implements Listener {

	private Set<CustomItem> customItems;

	public CustomItemManager() {
		customItems = new HashSet<CustomItem>();

	}

	@EventHandler
	public void onEvent(Event event) {
		customItems.forEach(customItem -> {
			customItem.runPassive(event);
		});
	}

}
