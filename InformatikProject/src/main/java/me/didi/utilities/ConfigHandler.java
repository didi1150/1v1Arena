package me.didi.utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import me.didi.MainClass;
import me.didi.gamesystem.gameStates.LobbyState;

public class ConfigHandler {

	private static FileConfiguration fileConfiguration;
	private static MainClass plugin;
	private static boolean setupFinished;
	private static List<Location> spawnLocations;

	private static ConfigHandler instance;

	public static void init(MainClass plugin) {
		instance = new ConfigHandler();

		ConfigHandler.plugin = plugin;
		ConfigHandler.fileConfiguration = ConfigHandler.plugin.getConfig();

		addDefaultValues();
		saveConfig();

		loadValues();

	}

	private static void addDefaultValues() {
		fileConfiguration.addDefault("lobby.min_players", 2);
		fileConfiguration.addDefault("lobby.max_players", 2);
		fileConfiguration.addDefault("setup_finished", false);

		fileConfiguration.options().copyDefaults(true);
	}

	private static void saveConfig() {
		ConfigHandler.plugin.saveConfig();
	}

	private static void loadValues() {

		int minPlayers = fileConfiguration.getInt("lobby.min_players", 2);
		int maxPlayers = fileConfiguration.getInt("lobby.max_players", 2);

		boolean setupFinished = fileConfiguration.getBoolean("setup_finished", false);
		ConfigHandler.setupFinished = setupFinished;

		if (fileConfiguration.contains("locations") && fileConfiguration.getConfigurationSection("locations") != null)
			spawnLocations = loadSpawnLocations();
		else
			spawnLocations = new ArrayList<Location>();

		LobbyState.MIN_PLAYERS = minPlayers;
		LobbyState.MAX_PLAYERS = maxPlayers;
	}

	private static List<Location> loadSpawnLocations() {
		List<Location> list = new ArrayList<>();

		for (String string : fileConfiguration.getConfigurationSection("locations").getKeys(false)) {

			double x = fileConfiguration.getDouble("locations." + string + ".x");
			double y = fileConfiguration.getDouble("locations." + string + ".y");
			double z = fileConfiguration.getDouble("locations." + string + ".z");
			World world = Bukkit.getWorld(fileConfiguration.getString("locations." + string + ".world"));
			float yaw = (float) fileConfiguration.getDouble("locations." + string + ".yaw");
			float pitch = (float) fileConfiguration.getDouble("locations." + string + ".pitch");

			Location location = new Location(world, x, y, z, yaw, pitch);
			list.add(location);
		}

		return list;
	}

	public List<Location> getSpawnLocations() {
		if (spawnLocations == null) {
			ChatUtils.sendDebugMessage("Du hast die Spawnpunkte noch nicht gesetzt!");
			return null;
		}
		return spawnLocations;
	}

	public boolean isSetupFinished() {
		return setupFinished;
	}

	public void setSetupFinished(boolean setupFinished) {
		ConfigHandler.setupFinished = setupFinished;
		fileConfiguration.set("setup_finished", setupFinished);
	}

	public void setSpawnLocations() {
		for (int i = 0; i < spawnLocations.size(); i++) {
			Location location = spawnLocations.get(i);

			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			String world = location.getWorld().getName();
			float yaw = location.getYaw();
			float pitch = location.getPitch();

			fileConfiguration.set("locations." + i + ".x", x);
			fileConfiguration.set("locations." + i + ".y", y);
			fileConfiguration.set("locations." + i + ".z", z);
			fileConfiguration.set("locations." + i + ".world", world);
			fileConfiguration.set("locations." + i + ".yaw", yaw);
			fileConfiguration.set("locations." + i + ".pitch", pitch);
		}

		saveConfig();
	}

	public void addSpawnLocation(Location spawnLocation) {
		if (spawnLocations.contains(spawnLocation))
			return;
		spawnLocations.add(spawnLocation);
	}

	public static ConfigHandler getInstance() {
		return instance;
	}

}
