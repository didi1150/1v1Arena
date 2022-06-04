package me.didi.events.damageSystem;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.didi.player.effects.SpecialEffect;

public class CustomEffectEvent extends Event {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private SpecialEffect specialEffect;

	public CustomEffectEvent(SpecialEffect specialEffect) {
		this.specialEffect = specialEffect;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public SpecialEffect getSpecialEffect() {
		return specialEffect;
	}
}
