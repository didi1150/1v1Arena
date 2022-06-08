package me.didi.events.customEvents;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomDamageEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;

	private Entity entity;
	private Entity attacker;
	private DamageReason damageReason;
	private double damage;
	private boolean knockback;
	
	public CustomDamageEvent(Entity entity, Entity attacker, DamageReason damageReason, double damage, boolean knockback) {
		this.isCancelled = false;
		this.entity = entity;
		this.attacker = attacker;
		this.damageReason = damageReason;
		this.damage = damage;
		this.knockback = knockback;
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

	public DamageReason getDamageReason() {
		return damageReason;
	}

	public Entity getEntity() {
		return entity;
	}

	public Entity getAttacker() {
		return attacker;
	}

	public double getDamage() {
		return damage;
	}

	public boolean isKnockback() {
		return knockback;
	}
	
}
