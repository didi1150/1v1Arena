package me.didi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.didi.champion.ChampionsManager;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.commands.CommandManager;
import me.didi.events.listeners.BlockListener;
import me.didi.events.listeners.DeathListener;
import me.didi.events.listeners.DropItemListener;
import me.didi.events.listeners.EntityDamageListener;
import me.didi.events.listeners.HealListener;
import me.didi.events.listeners.InventoryListener;
import me.didi.events.listeners.JoinListener;
import me.didi.events.listeners.PickupListener;
import me.didi.events.listeners.PlayerInteractListener;
import me.didi.events.listeners.PlayerMoveListener;
import me.didi.events.listeners.QuitListener;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.items.CustomItemManager;
import me.didi.items.ItemPassiveCooldownManager;
import me.didi.menus.PlayerMenuUtility;
import me.didi.menus.ScoreboardHandler;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ConfigHandler;
import me.didi.utilities.ItemSetter;
import me.didi.utilities.TaskManager;

public class MainClass extends JavaPlugin {

	private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
	/**
	 * Plugin instance, incase there are schedulers
	 */
	private static MainClass plugin;

	private ArrayList<Player> alivePlayers;

	private GameStateManager gameStateManager;

	private ChampionsManager championsManager;

	private AbilityStateManager abilityStateManager;

	private CustomPlayerManager customPlayerManager;

	private SpecialEffectsManager specialEffectsManager;

	private ConfigHandler configHandler;

	private CurrentStatGetter currentStatGetter;

	private CustomItemManager customItemManager;

	@Override
	public void onEnable() {
		plugin = this;

		ConfigHandler.init(this);

		configHandler = ConfigHandler.getInstance();

		TaskManager.init(this);

		customItemManager = new CustomItemManager(this);

		alivePlayers = new ArrayList<Player>();
		ChampionsManager.init();
		championsManager = ChampionsManager.getInstance();

		specialEffectsManager = new SpecialEffectsManager(this);

		AbilityStateManager.init(championsManager, new ItemSetter());

		abilityStateManager = AbilityStateManager.getInstance();
		abilityStateManager.startBackGroundTask();

		CustomPlayerManager.init(this, abilityStateManager, customItemManager);
		customPlayerManager = CustomPlayerManager.getInstance();

		CurrentStatGetter.init(customItemManager);
		this.currentStatGetter = CurrentStatGetter.getInstance();

		ChampionsManager.registerChampions(abilityStateManager, specialEffectsManager, customPlayerManager, this);

		gameStateManager = new GameStateManager(this, customPlayerManager, championsManager, configHandler,
				customItemManager);
		gameStateManager.setGameState(GameState.LOBBY_STATE);

		ScoreboardHandler.init(customPlayerManager, championsManager);

		ItemPassiveCooldownManager.getInstance().startCounter();

		registerListeners();
		getCommand("project").setExecutor(new CommandManager(gameStateManager, configHandler));
	}

	@Override
	public void onDisable() {
		abilityStateManager.stopBackgroundTask();
		customPlayerManager.stopBackgroundTask();
		ItemPassiveCooldownManager.getInstance().stopCounter();

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
		pm.registerEvents(new JoinListener(this, gameStateManager, customPlayerManager, configHandler), this);
		pm.registerEvents(new QuitListener(this, gameStateManager, abilityStateManager, configHandler), this);
		pm.registerEvents(new PlayerInteractListener(championsManager, gameStateManager, abilityStateManager,
				specialEffectsManager, customItemManager), this);
		pm.registerEvents(new EntityDamageListener(gameStateManager, currentStatGetter), this);
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new DeathListener(this, customPlayerManager, gameStateManager), this);
		pm.registerEvents(new DropItemListener(), this);
		pm.registerEvents(new PickupListener(), this);
		pm.registerEvents(new HealListener(), this);
		pm.registerEvents(new PlayerMoveListener(), this);
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

	public ArrayList<Player> getAlivePlayers() {
		return alivePlayers;
	}
}
