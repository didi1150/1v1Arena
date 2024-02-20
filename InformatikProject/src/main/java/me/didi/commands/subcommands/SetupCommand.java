package me.didi.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.didi.commands.SubCommand;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ConfigHandler;

public class SetupCommand extends SubCommand {

	private ConfigHandler configHandler;

	public SetupCommand(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}

	@Override
	public String getName() {
		return "finish";
	}

	@Override
	public String getDescription() {
		return "Sets the setup value to finished or unfinished";
	}

	@Override
	public String getSyntax() {
		return "/project finish <value>";
	}

	@Override
	public void runCommand(Player player, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("true")) {
				configHandler.setSetupFinished(Boolean.parseBoolean(args[1]));

				configHandler.setSpawnLocations();
				ChatUtils.sendMessageToPlayer(player,
						ChatColor.YELLOW + "The Game is now playable!");
				
				Bukkit.shutdown();
			} else if (args[1].equalsIgnoreCase("false")) {
				configHandler.setSetupFinished(Boolean.parseBoolean(args[1]));

				ChatUtils.sendMessageToPlayer(player,
						ChatColor.YELLOW + "The Game is now in edit mode!");
				

				Bukkit.shutdown();
			}
		}
	}
}
