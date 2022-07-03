package me.didi.gamesystem.gameStates;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.didi.MainClass;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.EndingCountdown;
import me.didi.utilities.ChatUtils;

public class EndingState extends GameState {

	private EndingCountdown endingCountdown;
	private MainClass plugin;

	public EndingState(EndingCountdown endingCountdown, MainClass plugin) {
		this.endingCountdown = endingCountdown;
		this.plugin = plugin;
	}

	@Override
	public void start() {
		if (plugin.getAlivePlayers().size() == 1) {
			Player player = plugin.getAlivePlayers().get(0);
			sendVictory(player);
		}

		ChatUtils.broadCastMessage(
				ChatColor.YELLOW + "Der Server stoppt in " + ChatColor.GOLD + "15 " + ChatColor.YELLOW + "Sekunden!");
		endingCountdown.start();
	}

	private void sendVictory(Player player) {
		ChatUtils.broadCastMessage(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has won the game!");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
