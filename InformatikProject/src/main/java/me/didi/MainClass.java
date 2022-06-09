package me.didi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.didi.champion.ChampionsManager;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.commands.CommandManager;
import me.didi.commands.TestCommand;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.listeners.BlockListener;
import me.didi.events.listeners.DeathListener;
import me.didi.events.listeners.DropItemListener;
import me.didi.events.listeners.EntityDamageListener;
import me.didi.events.listeners.InventoryListener;
import me.didi.events.listeners.JoinListener;
import me.didi.events.listeners.HealListener;
import me.didi.events.listeners.PickupListener;
import me.didi.events.listeners.PlayerInteractListener;
import me.didi.events.listeners.QuitListener;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.menus.PlayerMenuUtility;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;
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

	private AbilityStateManager abilityCooldownManager;

	private CustomPlayerManager customPlayerManager;

	private DamageManager damageManager;

	private SpecialEffectsManager specialEffectsManager;

	@Override
	public void onEnable() {
		plugin = this;

		alivePlayers = new ArrayList<UUID>();

		championsManager = new ChampionsManager(this);

		damageManager = new DamageManager();

		specialEffectsManager = new SpecialEffectsManager(this);

		abilityCooldownManager = new AbilityStateManager(this, championsManager, new ItemManager());
		abilityCooldownManager.startBackGroundTask();

		customPlayerManager = new CustomPlayerManager(this, championsManager, abilityCooldownManager);

		championsManager.registerChampions(abilityCooldownManager, specialEffectsManager, customPlayerManager);

		gameStateManager = new GameStateManager(this, customPlayerManager, championsManager);
		gameStateManager.setGameState(GameState.LOBBY_STATE);

		registerListeners();
		getCommand("test").setExecutor(new TestCommand());
		getCommand("project").setExecutor(new CommandManager(gameStateManager));

	}

	@Override
	public void onDisable() {
		abilityCooldownManager.stopBackgroundTask();
		customPlayerManager.stopBackgroundTask();

		Bukkit.getWorlds().forEach(world -> {
			world.getEntities().forEach(entity -> {
				if (entity instanceof ArmorStand) {
					entity.remove();
				}
			});
		});
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new JoinListener(this, gameStateManager, customPlayerManager), this);
		pm.registerEvents(new QuitListener(this, gameStateManager, abilityCooldownManager, championsManager), this);
		pm.registerEvents(new PlayerInteractListener(this, championsManager, gameStateManager), this);
		pm.registerEvents(new EntityDamageListener(customPlayerManager, damageManager, gameStateManager), this);
		pm.registerEvents(new HealListener(), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new DeathListener(this, customPlayerManager, gameStateManager), this);
		pm.registerEvents(new DropItemListener(), this);
		pm.registerEvents(new PickupListener(), this);
		pm.registerEvents(new HealListener(), this);
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
}
