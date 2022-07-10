package me.didi.events.customEvents;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.didi.champion.ability.AbilityType;

public class AbilityCastEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;

	private Entity from;
	private AbilityType abilityType;

	public AbilityCastEvent(Entity from, AbilityType abilityType) {
		this.from = from;
		this.abilityType = abilityType;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public AbilityType getAbilityType() {
		return abilityType;
	}

	public Entity getFrom() {
		return from;
	}

}
