package me.didi.gamesystem.gameStates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.countdowns.ItemSelectCountdown;
import me.didi.utilities.ItemBuilder;

public class ItemSelectState extends GameState {

	private ItemSelectCountdown countdown;
	
	public ItemSelectState(ItemSelectCountdown countdown) {
		this.countdown = countdown;
	}

	@Override
	public void start() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.getInventory().clear();
			player.getInventory().setItem(8,
					new ItemBuilder(Material.CHEST).setDisplayName(ChatColor.GOLD + "Select your items").toItemStack());
		});
		countdown.start();
	}

	@Override
	public void stop() {

	}

}
