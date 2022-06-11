package me.didi.events.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.didi.MainClass;
import me.didi.champion.Champion;
import me.didi.champion.ChampionsManager;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.menus.ChampionSelectMenu;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.TaskManager;

public class PlayerInteractListener implements Listener {

	private ChampionsManager championsManager;
	private GameStateManager gameStateManager;
	private AbilityStateManager abilityStateManager;
	private SpecialEffectsManager specialEffectsManager;

	private List<Player> cooldowns;

	public PlayerInteractListener(ChampionsManager championsManager, GameStateManager gameStateManager,
			AbilityStateManager abilityStateManager, SpecialEffectsManager specialEffectsManager) {
		this.championsManager = championsManager;
		this.gameStateManager = gameStateManager;
		this.cooldowns = new ArrayList<Player>();
		this.abilityStateManager = abilityStateManager;
		this.specialEffectsManager = specialEffectsManager;
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
						MainClass.getPlayerMenuUtility(event.getPlayer()), championsManager);
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
				if (cooldowns.contains(player))
					return;
				cooldowns.add(player);
				TaskManager.getInstance().runTaskLater(15, task -> {
					cooldowns.remove(player);
				});

				int slot = player.getInventory().getHeldItemSlot();
				Champion champion = championsManager.getSelectedChampion(player);
				if (slot < 4)
					champion.getAbilities()[slot].execute(abilityStateManager, player, specialEffectsManager);
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
