package me.didi.gamesystem.gameStates;

import org.bukkit.Bukkit;

import me.didi.gamesystem.GameState;
import me.didi.menus.ScoreboardHandler;

public class IngameState extends GameState {

	@Override
	public void start() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			ScoreboardHandler.getInstance().setScoreboard(player);
		});
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
