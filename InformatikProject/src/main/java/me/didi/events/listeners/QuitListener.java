package me.didi.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.didi.MainClass;
import me.didi.champion.ChampionsManager;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class QuitListener implements Listener {

	private MainClass plugin;

	private GameStateManager gameStateManager;
	private AbilityStateManager abilityCooldownManager;
	private ChampionsManager championsManager;

	public QuitListener(MainClass plugin, GameStateManager gameStateManager, AbilityStateManager abilityCooldownManager,
			ChampionsManager championsManager) {
		this.plugin = plugin;
		this.gameStateManager = gameStateManager;
		this.abilityCooldownManager = abilityCooldownManager;
		this.championsManager = championsManager;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		if (gameStateManager.getCurrentGameState() instanceof LobbyState) {

			plugin.getAlivePlayers().remove(player.getUniqueId());
			ChatUtils.broadCastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN
					+ " hat das Spiel verlassen! " + ChatColor.GOLD + "[" + plugin.getAlivePlayers().size()
					+ ChatColor.GRAY + "/" + LobbyState.MAX_PLAYERS + ChatColor.GOLD + "]");
			LobbyState lobbyState = (LobbyState) gameStateManager.getCurrentGameState();
			LobbyCountdown countdown = lobbyState.getCountdown();

			if (plugin.getAlivePlayers().size() < LobbyState.MIN_PLAYERS) {
				if (countdown.isRunning()) {
					countdown.stop();
					countdown.startIdle();
				}
			}
		} else {
			if (plugin.getAlivePlayers().contains(player.getUniqueId()))
				plugin.getAlivePlayers().remove(player.getUniqueId());

			for (int i = 0; i < championsManager.getSelectedChampion(player).getAbilities().length; i++) {
				Ability ability = championsManager.getSelectedChampion(player).getAbilities()[i];
				abilityCooldownManager.removeRecastCooldown(player, ability, i);
				abilityCooldownManager.removeCooldown(player);
			}

			if (plugin.getAlivePlayers().size() <= 1 && gameStateManager.getCurrentGameState() instanceof IngameState) {
				gameStateManager.setGameState(GameState.ENDING_STATE);
			}
		}
	}

}
