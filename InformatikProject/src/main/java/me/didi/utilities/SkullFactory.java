package me.didi.utilities;

import org.bukkit.ChatColor;

public enum SkullFactory {

	HEAD_GHOST(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBkNWFiMjFkNjdlZWFkMTYzMzE3YzlhNWRjNTFkNDA5ZDg1ZGYyNTI3NjY3YTQyNGEyYWZmNjczOGI2ZmM5NyJ9fX0=",
			"Ghost", ChatColor.GOLD);

	String value, name;
	ChatColor color;

	private SkullFactory(String title, String name) {
		this.value = title;
		this.name = name;
	}

	public String getTitle() {
		return value;
	}

	private SkullFactory(String title, String name, ChatColor color) {
		this.value = title;
		this.name = name;
		this.color = color;
	}

	private SkullFactory(String title) {
		this.value = title;
	}

	public ChatColor getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
}
