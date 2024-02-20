package me.didi.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.didi.commands.SubCommand;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ConfigHandler;

public class AddSpawnCommand extends SubCommand {

	private ConfigHandler configHandler;

	public AddSpawnCommand(ConfigHandler configHandler) {
		this.configHandler = configHandler;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "addSpawn";
	}

	@Override
	public String getDescription() {
		return "Adds your current location to the list of spawnLocations";
	}

	@Override
	public String getSyntax() {
		return "/project addSpawn";
	}

	@Override
	public void runCommand(Player player, String[] args) {
		configHandler.addSpawnLocation(player.getLocation());

		ChatUtils.sendMessageToPlayer(player,
				ChatColor.GREEN + "You have added a spawn location!");
	}

}
