package me.didi.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {

	public static final String prefix = ChatColor.GREEN + "[Bedwars] " + ChatColor.WHITE;

	public static void sendDebugMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "DEBUG: " + ChatColor.AQUA + message);
	}

	public static void sendMessageToPlayer(Player player, String message) {
		player.sendMessage(prefix + message);
	}

	public static void broadCastMessage(String message) {
		Bukkit.broadcastMessage(prefix + message);
	}
}
