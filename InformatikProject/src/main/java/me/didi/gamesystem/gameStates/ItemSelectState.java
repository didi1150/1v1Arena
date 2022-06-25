package me.didi.gamesystem.gameStates;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.ItemSelectStateCountdown;

public class ItemSelectState extends GameState {

	private ItemSelectStateCountdown countdown;

	@Override
	public void start() {
		countdown.start();
	}

	@Override
	public void stop() {

	}

}
