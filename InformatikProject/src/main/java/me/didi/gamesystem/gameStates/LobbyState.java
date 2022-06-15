package me.didi.gamesystem.gameStates;

import org.bukkit.ChatColor;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.utilities.ChatUtils;

public class LobbyState extends GameState {

	public static int MAX_PLAYERS = 2;
	public static int MIN_PLAYERS = 1;

	private LobbyCountdown countdown;

	public LobbyState(LobbyCountdown countdown) {
		this.countdown = countdown;
	}

	@Override
	public void start() {
		countdown.startIdle();
	}

	@Override
	public void stop() {
		ChatUtils.sendDebugMessage(ChatColor.YELLOW + "IngameState started!");
	}

	public LobbyCountdown getCountdown() {
		return countdown;
	}

}
