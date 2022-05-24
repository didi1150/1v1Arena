package me.didi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.didi.ability.AbilityCooldownManager;
import me.didi.events.listeners.InventoryListener;
import me.didi.events.listeners.JoinListener;
import me.didi.events.listeners.PlayerInteractListener;
import me.didi.events.listeners.QuitListener;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.menus.PlayerMenuUtility;
import me.didi.utilities.ItemManager;

public class MainClass extends JavaPlugin {

	private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
	/**
	 * Plugin instance, incase there are schedulers
	 */
	private static MainClass plugin;

	private ArrayList<UUID> alivePlayers;

	private GameStateManager gameStateManager;

	private ChampionsManager championsManager;

	private AbilityCooldownManager abilityCooldownManager;

	@Override
	public void onEnable() {
		plugin = this;

		alivePlayers = new ArrayList<UUID>();
		gameStateManager = new GameStateManager(this);
		gameStateManager.setGameState(GameState.LOBBY_STATE);
		championsManager = new ChampionsManager();
		abilityCooldownManager = new AbilityCooldownManager(this, new ItemManager());
		abilityCooldownManager.startBackGroundTask();

		registerListeners();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		abilityCooldownManager.stopBackgroundTask();
		super.onDisable();
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new QuitListener(this), this);
		pm.registerEvents(new PlayerInteractListener(this), this);
	}

	public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
		PlayerMenuUtility playerMenuUtility;
		if (!(playerMenuUtilityMap.containsKey(p))) {

			playerMenuUtility = new PlayerMenuUtility(p);
			playerMenuUtilityMap.put(p, playerMenuUtility);

			return playerMenuUtility;
		} else {
			return playerMenuUtilityMap.get(p);
		}
	}

	public static MainClass getPlugin() {
		return plugin;
	}

	public ArrayList<UUID> getAlivePlayers() {
		return alivePlayers;
	}

	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}

	public ChampionsManager getChampionsManager() {
		return championsManager;
	}

	public AbilityCooldownManager getAbilityCooldownManager() {
		return abilityCooldownManager;
	}
}
