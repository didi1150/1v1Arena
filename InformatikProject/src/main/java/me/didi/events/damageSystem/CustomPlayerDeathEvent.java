package me.didi.events.damageSystem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomPlayerDeathEvent extends Event {
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private Entity killer;

	private Player victim;

	public CustomPlayerDeathEvent(Entity killer, Player victim) {
		this.killer = killer;
		this.victim = victim;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public Entity getKiller() {
		return killer;
	}

	public Player getVictim() {
		return victim;
	}
}
