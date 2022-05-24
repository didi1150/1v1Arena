package me.didi.events.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.didi.MainClass;
import me.didi.characters.Champion;
import me.didi.gamesystem.gameStates.IngameState;
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
		} else if (plugin.getGameStateManager().getCurrentGameState() instanceof IngameState) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
					if (event.getItem().getType() == Material.INK_SACK && event.getItem().getDurability() == (short) 1)
						return;

					Player player = event.getPlayer();
					int slot = player.getInventory().getHeldItemSlot();
					Champion champion = plugin.getChampionsManager().getSelectedChampion(player);
					switch (slot) {
					case 0:
						champion.executeFirstAbility(player);
						break;
					case 1:
						champion.executeSecondAbility(player);
						break;
					case 2:
						champion.executeThirdAbility(player);
						break;
					case 3:
						champion.executeUltimate(player);
						break;
					}
				}
			}
		}

	}
}
