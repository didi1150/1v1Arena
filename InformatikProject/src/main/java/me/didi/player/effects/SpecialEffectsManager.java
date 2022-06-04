package me.didi.player.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.didi.MainClass;
import me.didi.events.damageSystem.CustomDamageEvent;

public class SpecialEffectsManager {

	private List<SpecialEffect> activeEffects;
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

	public SpecialEffectsManager(MainClass plugin) {
		Bukkit.getPluginManager().registerEvents(listener, plugin);
		this.activeEffects = new ArrayList<SpecialEffect>();
	}

	public void addSpecialEffect(SpecialEffect specialEffect) {
		specialEffect.setSpecialEffectsManager(this);
		activeEffects.add(specialEffect);
	}

	public void removeSpecialEffect(SpecialEffect specialEffect) {
		activeEffects.remove(specialEffect);
	}

	public void forwardEvent(Event event) {
		activeEffects.forEach(effect -> {
			effect.handleEvent(event);
		});
	}
}
