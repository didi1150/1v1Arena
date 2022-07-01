package me.didi.events.customEvents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.didi.player.CustomPlayer;

public class CustomPlayerHealthChangeEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;

	private CustomPlayer customPlayer;
	private float oldHealth;
	private float changedHealth;

	public CustomPlayerHealthChangeEvent(CustomPlayer customPlayer, float oldHealth, float changedHealth) {
		this.customPlayer = customPlayer;
		this.oldHealth = oldHealth;
		this.changedHealth = changedHealth;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public CustomPlayer getCustomPlayer() {
		return customPlayer;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public float getOldHealth() {
		return oldHealth;
	}

	public float getChangedHealth() {
		return changedHealth;
	}

}
