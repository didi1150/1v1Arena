package me.didi.gamesystem.gameStates;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.LobbyCountdown;
import me.didi.utilities.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class LobbyState extends GameState {

	public static int MAX_PLAYERS = 2;
	public static int MIN_PLAYERS = 1;

	private LobbyCountdown countdown;

	public LobbyState(LobbyCountdown countdown) {
		this.countdown = countdown;
	}

	@Override
	public void start() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.getInventory().clear();
			player.getInventory().setItem(8,
					new ItemBuilder(Material.CHEST).setDisplayName(ChatColor.GOLD + "Champion Select")
							.setLore(ChatColor.GRAY + "Select your favourite Champion!").toItemStack());

		});
		countdown.startIdle();
	}

	@Override
	public void stop() {
	}

	public LobbyCountdown getCountdown() {
		return countdown;
	}

}
