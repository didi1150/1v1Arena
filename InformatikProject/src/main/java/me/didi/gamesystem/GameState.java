package me.didi.gamesystem;

public abstract class GameState {

	public static final int LOBBY_STATE = 0, ITEM_SELECT_STATE = 1, INGAME_STATE = 2, ENDING_STATE = 3;
	
	public abstract void start();
	public abstract void stop();

}
