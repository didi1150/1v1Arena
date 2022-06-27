package me.didi.gamesystem.gameStates;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.ItemSelectCountdown;

public class ItemSelectState extends GameState {

	private ItemSelectCountdown countdown;

	@Override
	public void start() {
		countdown.start();
	}

	@Override
	public void stop() {

	}

}
