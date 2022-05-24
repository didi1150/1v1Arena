package me.didi.events.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.didi.MainClass;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.menus.ChampionSelectMenu;

public class PlayerInteractListener implements Listener {

	private MainClass plugin;

	public PlayerInteractListener(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getItem() != null && event.getItem().getType() != Material.AIR
						&& event.getItem().getItemMeta().getDisplayName().contains("Champion")) {
					ChampionSelectMenu championSelectMenu = new ChampionSelectMenu(
							MainClass.getPlayerMenuUtility(event.getPlayer()), plugin);
					championSelectMenu.open();
				}
			}
		}

	}
}
