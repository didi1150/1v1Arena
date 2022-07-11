package me.didi.events.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomShieldCastEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;

	private Player target;
	private Player from;

	private double shieldAmount;

	public CustomShieldCastEvent(Player target, Player from, double shieldAmount) {
		this.target = target;
		this.from = from;
		this.shieldAmount = shieldAmount;
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

	public Player getTarget() {
		return target;
	}

	public Player getFrom() {
		return from;
	}

	public double getShieldAmount() {
		return shieldAmount;
	}

}
