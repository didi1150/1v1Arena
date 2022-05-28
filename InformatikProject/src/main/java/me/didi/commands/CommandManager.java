package me.didi.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.didi.commands.subcommands.StartCommand;

public class CommandManager implements TabExecutor {

	private ArrayList<SubCommand> subcommands = new ArrayList<>();

	public CommandManager() {
		subcommands.add(new StartCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length > 0) {
				for (int i = 0; i < getSubcommands().size(); i++) {
					if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
						getSubcommands().get(i).runCommand(player, args);
					}
				}
			} else if (args.length == 0) {
				player.sendMessage("--------------------------------");
				for (int i = 0; i < getSubcommands().size(); i++) {
					player.sendMessage(
							getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
				}
				player.sendMessage("--------------------------------");
			}

		}

		return true;
	}

	public ArrayList<SubCommand> getSubcommands() {
		return subcommands;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return null;
	}

}
