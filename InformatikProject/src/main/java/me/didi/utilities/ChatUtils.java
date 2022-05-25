package me.didi.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

public class ChatUtils {

	public static final String prefix = ChatColor.GREEN + "[InformatikProjekt] " + ChatColor.WHITE;

	public static void sendDebugMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "DEBUG: " + ChatColor.AQUA + message);
	}

	public static void sendMessageToPlayer(Player player, String message) {
		player.sendMessage(prefix + message);
	}

	public static void broadCastMessage(String message) {
		Bukkit.broadcastMessage(prefix + message);
	}

	public static void sendActionBar(Player player, String message) {
		CraftPlayer cp = (CraftPlayer) player;
		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		cp.getHandle().playerConnection.sendPacket(ppoc);
	}

	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		PacketPlayOutTitle times;
		if (title != null) {
			times = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
					CraftChatMessage.fromString(title)[0]);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(times);
		}
		if (subtitle != null) {
			times = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
					CraftChatMessage.fromString(subtitle)[0]);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(times);
		}
		times = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(times);
	}
}
