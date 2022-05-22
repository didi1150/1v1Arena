package me.didi.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.didi.MainClass;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {

	private MainClass plugin;

	public JoinListener(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState))
			return;
		Player player = event.getPlayer();
		plugin.getAlivePlayers().add(player.getUniqueId());
		ChatUtils.broadCastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN
				+ " ist dem Spiel beigetreten! " + ChatColor.GOLD + "[" + plugin.getAlivePlayers().size()
				+ ChatColor.GRAY + "/" + LobbyState.MAX_PLAYERS + ChatColor.GOLD + "]");

		LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
		LobbyCountdown countdown = lobbyState.getCountdown();

		if (plugin.getAlivePlayers().size() >= LobbyState.MIN_PLAYERS) {
			if (!countdown.isRunning()) {
				countdown.stopIdle();
				countdown.start();
			}
		}
	}

}
