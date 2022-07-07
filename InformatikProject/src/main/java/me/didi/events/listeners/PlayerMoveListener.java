package me.didi.events.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import me.didi.menus.ScoreboardHandler;

public class PlayerMoveListener implements Listener {

	public static Map<Player, Vector> vectors = new HashMap<>();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		double xDiff = event.getTo().getX() - event.getFrom().getX();
		double zDiff = event.getTo().getZ() - event.getFrom().getZ();

		if (xDiff > 0 || zDiff > 0) {
			Vector vector = new Vector(xDiff, 0, zDiff);
			vectors.put(event.getPlayer(), vector);
		} else {
			vectors.remove(event.getPlayer());
		}

		ScoreboardHandler.getInstance().updateMoveSpeed(event.getPlayer());
	}

}
