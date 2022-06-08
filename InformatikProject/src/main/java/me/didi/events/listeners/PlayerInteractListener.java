package me.didi.events.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.didi.MainClass;
import me.didi.champion.Champion;
import me.didi.champion.ChampionsManager;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.menus.ChampionSelectMenu;

public class PlayerInteractListener implements Listener {

	private MainClass plugin;

	private ChampionsManager championsManager;
	private GameStateManager gameStateManager;

	public PlayerInteractListener(MainClass plugin, ChampionsManager championsManager,
			GameStateManager gameStateManager) {
		this.plugin = plugin;
		this.championsManager = championsManager;
		this.gameStateManager = gameStateManager;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (gameStateManager.getCurrentGameState() instanceof LobbyState) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				if (event.getItem() == null)
					return;
				if (event.getItem().getType() == Material.AIR)
					return;
				if (!event.getItem().hasItemMeta())
					return;
				if (!event.getItem().getItemMeta().hasDisplayName())
					return;
				if (!event.getItem().getItemMeta().getDisplayName().contains("Champion"))
					return;

				ChampionSelectMenu championSelectMenu = new ChampionSelectMenu(
						MainClass.getPlayerMenuUtility(event.getPlayer()), plugin, championsManager);
				championSelectMenu.open();
			}
		} else if (gameStateManager.getCurrentGameState() instanceof IngameState) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

				if (event.getItem() == null)
					return;
				if (event.getItem().getType() == Material.AIR)
					return;
				if (event.getItem().getType() == Material.INK_SACK && event.getItem().getDurability() == (short) 1)
					return;
				if (event.getItem().getType() == Material.BARRIER)
					return;

				Player player = event.getPlayer();
				int slot = player.getInventory().getHeldItemSlot();
				Champion champion = championsManager.getSelectedChampion(player);
				switch (slot) {
				case 0:
					champion.executeFirstAbility();
					break;
				case 1:
					champion.executeSecondAbility();
					break;
				case 2:
					champion.executeThirdAbility();
					break;
				case 3:
					champion.executeUltimate();
					break;
				}
			} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

				if (event.getItem() == null)
					return;
				if (event.getItem().getType() == Material.AIR)
					return;
				if (event.getItem().getType() == Material.INK_SACK && event.getItem().getDurability() == (short) 1)
					return;
				if (event.getItem().getType() == Material.BARRIER)
					return;

				Player player = event.getPlayer();
				int slot = player.getInventory().getHeldItemSlot();
				Champion champion = championsManager.getSelectedChampion(player);
				if (slot == 4) {
					champion.executeAutoAttack();
				}
			}
		}

	}
}
