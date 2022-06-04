package me.didi.player.effects;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

public abstract class SpecialEffect {

	protected Entity from;
	protected Entity to;
	private SpecialEffectsManager specialEffectsManager;
	protected double duration;

	public SpecialEffect(Entity from, Entity to, double duration) {
		this.from = from;
		this.to = to;
		this.duration = duration;
	}

	public void setSpecialEffectsManager(SpecialEffectsManager specialEffectsManager) {
		this.specialEffectsManager = specialEffectsManager;
	}

	public SpecialEffectsManager getSpecialEffectsManager() {
		return specialEffectsManager;
	}

	public Entity getFrom() {
		return from;
	}

	public Entity getTo() {
		return to;
	}

	public double getDuration() {
		return duration;
	}

	public abstract void handleEvent(Event event);

	public void endEffect() {
		specialEffectsManager.removeSpecialEffect(this);
	}
}
