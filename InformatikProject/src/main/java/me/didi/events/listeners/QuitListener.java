package me.didi.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class QuitListener implements Listener {

	private MainClass plugin;

	public QuitListener(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		if (plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {

			plugin.getAlivePlayers().remove(player.getUniqueId());
			ChatUtils.broadCastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN
					+ " hat das Spiel verlassen! " + ChatColor.GOLD + "[" + plugin.getAlivePlayers().size()
					+ ChatColor.GRAY + "/" + LobbyState.MAX_PLAYERS + ChatColor.GOLD + "]");
			LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
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

			for (Ability ability : plugin.getChampionsManager().getSelectedChampion(player).getAbilities()) {
				plugin.getAbilityCooldownManager().removeRecastCooldown(player, ability);
				plugin.getAbilityCooldownManager().removeCooldown(player);
			}

			if (plugin.getAlivePlayers().size() <= 1
					&& plugin.getGameStateManager().getCurrentGameState() instanceof IngameState) {
				plugin.getGameStateManager().setGameState(GameState.ENDING_STATE);
			}
		}
	}

}
