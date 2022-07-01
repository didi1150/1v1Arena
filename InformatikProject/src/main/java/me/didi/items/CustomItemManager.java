package me.didi.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.didi.items.impl.WITS_END;

public class CustomItemManager implements Listener {

	private List<CustomItem> customItems;
	private Map<Player, Set<CustomItem>> selectedItems;

	public CustomItemManager() {
		customItems = new ArrayList<CustomItem>();
		selectedItems = new HashMap<Player, Set<CustomItem>>();
		customItems.add(new WITS_END());

	}

	@EventHandler
	public void onEvent(Event event) {
		selectedItems.entrySet().forEach(entry -> {
			Player player = entry.getKey();
			Set<CustomItem> selection = entry.getValue();

			selection.forEach(item -> item.getItemPassives()
					.forEach(passive -> passive.runPassive(event, player, item.getSlot())));

		});
	}

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
}
