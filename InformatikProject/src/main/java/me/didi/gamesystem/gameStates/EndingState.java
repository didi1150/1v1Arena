package me.didi.gamesystem.gameStates;

import org.bukkit.ChatColor;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.EndingCountdown;
import me.didi.utilities.ChatUtils;

public class EndingState extends GameState {

	private EndingCountdown endingCountdown;

	public EndingState(EndingCountdown endingCountdown) {
		this.endingCountdown = endingCountdown;
	}

	@Override
	public void start() {
		ChatUtils.broadCastMessage(ChatColor.YELLOW + "Der Server started neu in " + ChatColor.GOLD + "15 "
				+ ChatColor.YELLOW + "Sekunden!");
		endingCountdown.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
