package me.didi.player.effects;

import java.util.function.Consumer;

import org.bukkit.entity.Entity;

public abstract class SpecialEffect {

	private Entity from;
	private Entity to;
	private Consumer<Entity> callback;
	private String eventName;

	public SpecialEffect(Entity from, Entity to, Consumer<Entity> callback, String eventName) {
		this.from = from;
		this.to = to;
		this.callback = callback;
		this.eventName = eventName;
	}

	public void run() {
		callback.accept(to);
	}

	public Entity getFrom() {
		return from;
	}

	public Entity getTo() {
		return to;
	}

	public String getEventName() {
		return eventName;
	}

}
