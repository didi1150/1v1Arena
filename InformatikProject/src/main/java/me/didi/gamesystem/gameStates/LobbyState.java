package me.didi.gamesystem.gameStates;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.LobbyCountdown;

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
	}

	public LobbyCountdown getCountdown() {
		return countdown;
	}

}
