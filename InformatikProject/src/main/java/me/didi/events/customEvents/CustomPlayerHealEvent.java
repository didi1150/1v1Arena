package me.didi.events.customEvents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.didi.player.CustomPlayer;

public class CustomPlayerHealEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;

	private CustomPlayer customPlayer;
	private HealReason healReason;
	private float healAmount;

	public CustomPlayerHealEvent(CustomPlayer customPlayer, HealReason healReason, float healAmount) {
		this.customPlayer = customPlayer;
		this.healReason = healReason;
		this.healAmount = healAmount;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public HealReason getHealReason() {
		return healReason;
	}

	public CustomPlayer getCustomPlayer() {
		return customPlayer;
	}

	public float getHealAmount() {
		return healAmount;
	}
	
	public void setHealAmount(float healAmount) {
		this.healAmount = healAmount;
	}
}
