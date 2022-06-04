package me.didi.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.didi.events.damageSystem.CustomEffectEvent;

public class SpecialEffectsListener implements Listener {

	@EventHandler
	public void onSpell(CustomEffectEvent event) {
		event.getSpecialEffect().run();
	}

}
