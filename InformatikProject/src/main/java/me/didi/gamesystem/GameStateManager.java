package me.didi.gamesystem;

import me.didi.MainClass;
import me.didi.champion.ChampionsManager;
import me.didi.gamesystem.countdowns.EndingCountdown;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.EndingState;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.ItemSelectState;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ConfigHandler;

public class GameStateManager {

	private MainClass plugin;

	private GameState[] gameStates;

	private GameState currentGameState;

	private ConfigHandler configHandler;

	public GameStateManager(MainClass plugin, CustomPlayerManager customPlayerManager,
			ChampionsManager championsManager, ConfigHandler configHandler) {
		this.plugin = plugin;
		this.configHandler = configHandler;
		this.gameStates = new GameState[4];

		gameStates[GameState.LOBBY_STATE] = new LobbyState(new LobbyCountdown(this, championsManager, configHandler));
		gameStates[GameState.ITEM_SELECT_STATE] = new ItemSelectState();
		gameStates[GameState.INGAME_STATE] = new IngameState(configHandler, customPlayerManager, championsManager);
		gameStates[GameState.ENDING_STATE] = new EndingState(new EndingCountdown(this, customPlayerManager), plugin);
	}

	public void setGameState(int gameStateID) {
		if (currentGameState != null)
			currentGameState.stop();
		currentGameState = gameStates[gameStateID];
		if (configHandler.isSetupFinished())
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
