package me.didi.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.didi.MainClass;
import me.didi.commands.SubCommand;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;

public class StartCommand extends SubCommand {

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Lowers the lobby countdown";
	}

	@Override
	public String getSyntax() {
		return "/project start";
	}

	@Override
	public void runCommand(Player player, String[] args) {
		if (MainClass.getPlugin().getGameStateManager().getCurrentGameState() instanceof LobbyState) {
			LobbyState lobbyState = (LobbyState) MainClass.getPlugin().getGameStateManager().getCurrentGameState();
			if (lobbyState.getCountdown().isRunning() && lobbyState.getCountdown().getSeconds() > 1) {
				lobbyState.getCountdown().setSeconds(1);
				ChatUtils.sendMessageToPlayer(player,
						ChatColor.GREEN + "You have successfully lowered the remaining countdown");
			}
		}
	}

}
