package me.didi.utilities;

import org.bukkit.ChatColor;

public enum SkullFactory {

	HEAD_GHOST(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBkNWFiMjFkNjdlZWFkMTYzMzE3YzlhNWRjNTFkNDA5ZDg1ZGYyNTI3NjY3YTQyNGEyYWZmNjczOGI2ZmM5NyJ9fX0=",
			"Ghost", ChatColor.GOLD),
	HEAD_BOMB(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U1NWM1YWJjZTdhNzkyNjRhNDg5MmQ2ODgwNmVhYWZlMzcwYTBlNGRkMjZmNTYxOTFmN2MxODhmMDFlZDcyNiJ9fX0=",
			"Bomb", ChatColor.GOLD),
	HEAD_ANAKIN(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNiYmE1NmM0ZmI4NjBmNzA4ZThlNjVkMTFiOWQxZmM4MzI5ODRhNTQwODgwMDQzODEwZTAwZWIwMzgwOTdmZiJ9fX0=",
			"Anakin", ChatColor.BLUE),
	HEAD_LLOYD(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYwOGIwOTkyYzVlNTdiZGQ2OWYzZTZmMjNhNzcyY2I4ODM4MWY4Y2NlYWUwMzU5MjRjMjI3ZGQ4YTNlOTJhZSJ9fX0=",
			"Lloyd", ChatColor.BLUE),
	HEAD_PERRY(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJhYWVjYmQ3MGQyZjBlNGZiZDY2NGMxYzMyNzQ1MGJkY2ZlMTlmMWE2Mzk5MTliZWQ5Y2QyY2ZjNjI0NTNlIn19fQ==",
			"Perry", ChatColor.BLUE),
	HEAD_REX(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODgxMGZiNjBkZmU1MmI2YjQ3MTAyMDg5ZDg0YjU1MWFkODM3NmQ3ZTk5NjczZmNlNTQxZmNiOTYyOWMxYjI1MSJ9fX0=",
			"Rex", ChatColor.BLUE),;

	String value, name;
	ChatColor color;

	private SkullFactory(String title, String name) {
		this.value = title;
		this.name = name;
	}

	public String getValue() {
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
