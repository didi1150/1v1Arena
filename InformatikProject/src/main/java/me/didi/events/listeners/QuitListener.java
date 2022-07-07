package me.didi.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.didi.MainClass;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.ItemSelectState;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ConfigHandler;
import net.md_5.bungee.api.ChatColor;

public class QuitListener implements Listener {

	private MainClass plugin;

	private GameStateManager gameStateManager;
	private AbilityStateManager abilityStateManager;

	private ConfigHandler configHandler;

	public QuitListener(MainClass plugin, GameStateManager gameStateManager, AbilityStateManager abilityStateManager,
			ConfigHandler configHandler) {
		this.plugin = plugin;
		this.gameStateManager = gameStateManager;
		this.abilityStateManager = abilityStateManager;
		this.configHandler = configHandler;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (!configHandler.isSetupFinished())
			return;

		event.setQuitMessage(null);
		Player player = event.getPlayer();
		if (gameStateManager.getCurrentGameState() instanceof LobbyState
				|| gameStateManager.getCurrentGameState() instanceof ItemSelectState) {

			plugin.getAlivePlayers().remove(player);
			ChatUtils.broadCastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN
					+ " hat das Spiel verlassen! " + ChatColor.GOLD + "[" + plugin.getAlivePlayers().size()
					+ ChatColor.GRAY + "/" + LobbyState.MAX_PLAYERS + ChatColor.GOLD + "]");
			if (gameStateManager.getCurrentGameState() instanceof ItemSelectState) {
				ChatUtils.broadCastMessage(ChatColor.YELLOW + "Item Select has been cancelled: " + ChatColor.AQUA
						+ player.getName() + ChatColor.YELLOW + " has left.");
				gameStateManager.setGameState(GameState.LOBBY_STATE);
				return;
			}

			LobbyState lobbyState = (LobbyState) gameStateManager.getCurrentGameState();
			LobbyCountdown countdown = lobbyState.getCountdown();

			if (plugin.getAlivePlayers().size() < LobbyState.MAX_PLAYERS) {
				if (countdown.isRunning()) {
					countdown.stop();
					countdown.startIdle();
				}
			}
		} else {
			if (plugin.getAlivePlayers().contains(player))
				plugin.getAlivePlayers().remove(player);

			abilityStateManager.removeCooldowns(player);

			if (plugin.getAlivePlayers().size() <= 1 && gameStateManager.getCurrentGameState() instanceof IngameState) {
				gameStateManager.setGameState(GameState.ENDING_STATE);
			}
		}
	}

}
