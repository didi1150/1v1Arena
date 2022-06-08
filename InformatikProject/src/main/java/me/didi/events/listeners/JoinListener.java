package me.didi.events.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import me.didi.MainClass;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {

	private MainClass plugin;

	private GameStateManager gameStateManager;
	private CustomPlayerManager customPlayerManager;

	public JoinListener(MainClass plugin, GameStateManager gameStateManager, CustomPlayerManager customPlayerManager) {
		this.plugin = plugin;
		this.gameStateManager = gameStateManager;
		this.customPlayerManager = customPlayerManager;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if (gameStateManager.getCurrentGameState() instanceof LobbyState) {
			Player player = event.getPlayer();

			if (plugin.getAlivePlayers().size() >= LobbyState.MAX_PLAYERS) {
				player.kickPlayer("Sorry, we're full");
			}

			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[] { new ItemStack(Material.AIR),
					new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
			player.getActivePotionEffects().clear();
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
			player.setLevel(0);
			player.setExp(0);

			plugin.getAlivePlayers().add(player.getUniqueId());
			ChatUtils.broadCastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN
					+ " ist dem Spiel beigetreten! " + ChatColor.GOLD + "[" + plugin.getAlivePlayers().size()
					+ ChatColor.GRAY + "/" + LobbyState.MAX_PLAYERS + ChatColor.GOLD + "]");

			LobbyState lobbyState = (LobbyState) gameStateManager.getCurrentGameState();
			LobbyCountdown countdown = lobbyState.getCountdown();

			if (plugin.getAlivePlayers().size() >= LobbyState.MIN_PLAYERS) {
				if (!countdown.isRunning()) {
					countdown.stopIdle();
					countdown.start();
				}
			}

			player.getInventory().setItem(8,
					new ItemBuilder(new ItemStack(Material.CHEST)).setDisplayName(ChatColor.GOLD + "Champion Select")
							.setLore(ChatColor.GRAY + "Select your favourite Champion!").toItemStack());
		} else {
			Player player = event.getPlayer();
			customPlayerManager.setGhost(player);
		}
	}

}
