package me.didi.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.didi.MainClass;
import me.didi.events.damageSystem.CustomPlayerDeathEvent;
import me.didi.gamesystem.GameState;
import me.didi.utilities.ChatUtils;

public class DeathListener implements Listener {

	private MainClass plugin;

	public DeathListener(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDeath(CustomPlayerDeathEvent event) {
//		Entity killer = event.getKiller();
		Player victim = event.getVictim();
		plugin.getAlivePlayers().remove(victim.getUniqueId());
		ChatUtils.sendTitle(victim, ChatColor.RED + "YOU DIED!", "", 5, 20 * 3, 5);
		ChatUtils.broadCastMessage(ChatColor.AQUA + victim.getName() + ChatColor.GOLD + " has died!");

		if (plugin.getAlivePlayers().size() == 1) {
			Player player = Bukkit.getPlayer(plugin.getAlivePlayers().get(0));
			sendVictory(player);
		}

		plugin.getCustomPlayerManager().setGhost(victim);
		plugin.getGameStateManager().setGameState(GameState.ENDING_STATE);

	}

	private void sendVictory(Player player) {
		ChatUtils.broadCastMessage(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has won the game!");
	}

}
