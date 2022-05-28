package me.didi.gamesystem.gameStates;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.EndingCountdown;

public class EndingState extends GameState{

	private EndingCountdown endingCountdown;
	
	public EndingState(EndingCountdown endingCountdown) {
		this.endingCountdown = endingCountdown;
	}

	@Override
	public void start() {
		endingCountdown.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
