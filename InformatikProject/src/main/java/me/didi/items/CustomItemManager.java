package me.didi.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.items.impl.STERAKS_GAGE;
import me.didi.items.impl.WITS_END;

public class CustomItemManager {

	private List<CustomItem> customItems;
	private Map<Player, Set<CustomItem>> selectedItems;

	public CustomItemManager(Plugin plugin) {
		customItems = new ArrayList<CustomItem>();
		selectedItems = new HashMap<Player, Set<CustomItem>>();
		customItems.add(new WITS_END());
		customItems.add(new STERAKS_GAGE());

		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}

	Listener listener = new Listener() {
		@EventHandler
		public void onMove(PlayerMoveEvent event) {
			forwardEvent(event);
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onDamage(CustomDamageEvent event) {
			forwardEvent(event);
		}
	};

	public List<CustomItem> getCustomItems() {
		return customItems;
	}

	public Map<Player, Set<CustomItem>> getSelectedItems() {
		return selectedItems;
	}

	public CustomItem isSame(ItemStack itemStack, CustomItem customItem) {
		for (CustomItem element : getCustomItems()) {
			if (element.getItemStack().getItemMeta().getDisplayName()
					.equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
				customItem = element;
				break;
			}
		}
		return customItem;
	}

	public void forwardEvent(Event event) {
		selectedItems.entrySet().forEach(entry -> {
			Player player = entry.getKey();
			Set<CustomItem> selection = entry.getValue();

			selection.forEach(item -> item.getItemPassives()
					.forEach(passive -> passive.runPassive(event, player, item.getSlot())));

		});
	}
}
