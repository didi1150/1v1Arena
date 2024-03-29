package me.didi.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.events.customEvents.CustomPlayerHealthChangeEvent;
import me.didi.events.customEvents.CustomShieldCastEvent;
import me.didi.items.impl.COSMIC_DRIVE;
import me.didi.items.impl.DEATHS_DANCE;
import me.didi.items.impl.ESSENCE_REAVER;
import me.didi.items.impl.LORD_DOMINIKS_REGARDS;
import me.didi.items.impl.NASHORS_TOOTH;
import me.didi.items.impl.RANDUINS_OMEN;
import me.didi.items.impl.SERYLDAS_GRUDGE;
import me.didi.items.impl.SPIRIT_VISAGE;
import me.didi.items.impl.STERAKS_GAGE;
import me.didi.items.impl.THE_COLLECTOR;
import me.didi.items.impl.THORNMAIL;
import me.didi.items.impl.WITS_END;
import me.didi.items.passives.BITTER_COLD;
import me.didi.items.passives.BOUNDLESS_VITALITY;
import me.didi.items.passives.DEATH;
import me.didi.items.passives.FRAY;
import me.didi.items.passives.GIANT_SLAYER;
import me.didi.items.passives.ICATHIAN_BITE;
import me.didi.items.passives.IGNORE_PAIN;
import me.didi.items.passives.LIFELINE;
import me.didi.items.passives.ROCK_SOLID;
import me.didi.items.passives.SPELLBLADE;
import me.didi.items.passives.SPELLDANCE;
import me.didi.items.passives.THORNS;

public class CustomItemManager {

	private List<CustomItem> customItems;
	private Map<Player, List<CustomItem>> selectedItems;

	public CustomItemManager(Plugin plugin) {
		customItems = new ArrayList<CustomItem>();
		selectedItems = new HashMap<Player, List<CustomItem>>();
		customItems.add(new WITS_END(Arrays.asList(new FRAY())));
		customItems.add(new STERAKS_GAGE(Arrays.asList(new LIFELINE())));
		customItems.add(new COSMIC_DRIVE(Arrays.asList(new SPELLDANCE())));
		customItems.add(new THORNMAIL(Arrays.asList(new THORNS())));
		customItems.add(new DEATHS_DANCE(Arrays.asList(new IGNORE_PAIN())));
		customItems.add(new ESSENCE_REAVER(Arrays.asList(new SPELLBLADE())));
		customItems.add(new SPIRIT_VISAGE(Arrays.asList(new BOUNDLESS_VITALITY())));
		customItems.add(new LORD_DOMINIKS_REGARDS(Arrays.asList(new GIANT_SLAYER())));
		customItems.add(new NASHORS_TOOTH(Arrays.asList(new ICATHIAN_BITE())));
		customItems.add(new RANDUINS_OMEN(Arrays.asList(new ROCK_SOLID())));
		customItems.add(new SERYLDAS_GRUDGE(Arrays.asList(new BITTER_COLD())));
		customItems.add(new THE_COLLECTOR(Arrays.asList(new DEATH())));
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}

	Listener listener = new Listener() {

		@EventHandler(priority = EventPriority.LOW)
		public void onShield(CustomShieldCastEvent event) {
			if (event.isCancelled())
				return;
			forwardEvent(event);
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onHeal(CustomPlayerHealEvent event) {
			if (event.isCancelled())
				return;
			forwardEvent(event);
		}

		@EventHandler
		public void onCast(AbilityCastEvent event) {
			if (event.isCancelled())
				return;
			forwardEvent(event);
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onDamage(CustomPlayerHealthChangeEvent event) {
			if (event.isCancelled())
				return;
			forwardEvent(event);
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onDamage(CustomDamageEvent event) {
			if (event.isCancelled())
				return;
			forwardEvent(event);
		}
	};

	public List<CustomItem> getCustomItems() {
		return customItems;
	}

	public Map<Player, List<CustomItem>> getSelectedItems() {
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
			List<CustomItem> selection = entry.getValue();

			for (int i = 0; i < selection.size(); i++) {
				CustomItem item = selection.get(i);

				int index = i;
				item.getItemPassives().forEach(passive -> {
					passive.runPassive(event, player, item.getSlot(), index);
				});
			}
		});
	}
}
