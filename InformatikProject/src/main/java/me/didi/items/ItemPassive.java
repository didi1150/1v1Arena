package me.didi.items;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface ItemPassive {

	void runPassive(Event event, Player player, int slot, int index);

	String getName();

	String[] getDescription();

	int getCooldown();

}
