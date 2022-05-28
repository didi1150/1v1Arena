package me.didi.gamesystem;

import me.didi.MainClass;
import me.didi.gamesystem.countdowns.EndingCountdown;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.EndingState;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.LobbyState;

public class GameStateManager {

	private MainClass plugin;

	private GameState[] gameStates;

	private GameState currentGameState;

	public GameStateManager(MainClass plugin) {
		this.plugin = plugin;
		this.gameStates = new GameState[3];

		gameStates[GameState.LOBBY_STATE] = new LobbyState(new LobbyCountdown(this));
		gameStates[GameState.INGAME_STATE] = new IngameState();
		gameStates[GameState.ENDING_STATE] = new EndingState(new EndingCountdown(this));
	}

	public void setGameState(int gameStateID) {
		if (currentGameState != null)
			currentGameState.stop();
		currentGameState = gameStates[gameStateID];
		currentGameState.start();
	}

	public void stopCurrentGameState() {
		if (currentGameState != null)
			currentGameState.stop();
		currentGameState = null;
	}

	public GameState getCurrentGameState() {
		return currentGameState;
	}
	
	public MainClass getPlugin() {
		return plugin;
	}

}
