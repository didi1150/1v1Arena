package me.didi.events.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.didi.MainClass;
import me.didi.events.customEvents.CustomPlayerDeathEvent;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ChatUtils;

public class DeathListener implements Listener {

	private MainClass plugin;
	private CustomPlayerManager customPlayerManager;
	private GameStateManager gameStateManager;

	public DeathListener(MainClass plugin, CustomPlayerManager customPlayerManager, GameStateManager gameStateManager) {
		this.plugin = plugin;
		this.customPlayerManager = customPlayerManager;
		this.gameStateManager = gameStateManager;
	}

	@EventHandler
	public void onDeath(CustomPlayerDeathEvent event) {
		Player victim = event.getVictim();
		plugin.getAlivePlayers().remove(victim.getUniqueId());
		ChatUtils.sendTitle(victim, ChatColor.RED + "YOU DIED!", "", 5, 20 * 3, 5);
		ChatUtils.broadCastMessage(ChatColor.AQUA + victim.getName() + ChatColor.GOLD + " has died!");

		customPlayerManager.setGhost(victim);
		gameStateManager.setGameState(GameState.ENDING_STATE);

	}
}
