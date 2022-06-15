package me.didi.gamesystem.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.ChampionsManager;
import me.didi.champion.ability.Ability;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ConfigHandler;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;

public class LobbyCountdown extends Countdown {

	private static final int COUNTDOWN_TIME = 20, IDLE_TIME = 20 * 15;
	private BukkitTask idleTask;
	private GameStateManager gameStateManager;
	private boolean isIdling;

	private ConfigHandler configHandler;
	private CustomPlayerManager customPlayerManager;
	private ChampionsManager championsManager;

	private ChatColor[] countdownColours = new ChatColor[] { ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
			ChatColor.GREEN, ChatColor.AQUA };

	private int seconds;
	private boolean isRunning;

	public LobbyCountdown(GameStateManager gameStateManager, CustomPlayerManager customPlayerManager,
			ChampionsManager championsManager, ConfigHandler configHandler) {
		this.gameStateManager = gameStateManager;
		this.configHandler = configHandler;
		this.seconds = COUNTDOWN_TIME;
		this.customPlayerManager = customPlayerManager;
		this.championsManager = championsManager;
	}

	@Override
	public void start() {
		this.isRunning = true;

		this.bukkitTask = TaskManager.getInstance().repeatUntil(20, 20, COUNTDOWN_TIME * 20, (task, counter) -> {

			if (seconds % 10 == 0) {
				ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.GOLD + seconds
						+ ChatColor.YELLOW + " Sekunden!");
			}
			if (seconds <= 5 && seconds > 0) {
				if (seconds == 1) {
					ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.GOLD + seconds
							+ ChatColor.YELLOW + " Sekunde!");
				} else {

					ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.GOLD + seconds
							+ ChatColor.YELLOW + " Sekunden!");
				}
				Bukkit.getOnlinePlayers().forEach(player -> {
					ChatUtils.sendTitle(player, countdownColours[seconds - 1] + "" + seconds, "", 0, 20, 0);

				});

			}

			if (seconds == 0) {

				int index = 0;

				for (Player player : Bukkit.getOnlinePlayers()) {
					player.getInventory().clear();
					Ability[] abilities = championsManager.getSelectedChampion(player).getAbilities();
					for (int i = 0; i < abilities.length; i++) {
						player.getInventory().setItem(i, abilities[i].getIcon());
					}

					ItemStack autoAttackItem = championsManager.getSelectedChampion(player).getAutoAttackItem();

					player.getInventory().setItem(4, autoAttackItem);
					for (int i = 5; i < 9; i++) {
						player.getInventory().setItem(i,
								new ItemBuilder(new ItemStack(Material.BARRIER)).setDisplayName(ChatColor.RED + "NA")
										.setLore(ChatColor.GRAY + "This slot is not available!").toItemStack());
					}
					customPlayerManager.addPlayer(player);
					player.getInventory().setHelmet(championsManager.getSelectedChampion(player).getIcon());

					if (configHandler.getSpawnLocations() != null)
						player.teleport(configHandler.getSpawnLocations().get(index));
					index++;
				}

				customPlayerManager.startBackgroundTask();
				gameStateManager.setGameState(GameState.INGAME_STATE);
				stop();
			}

			Bukkit.getOnlinePlayers().forEach(player -> {
				player.setLevel(seconds);
				player.setExp(((float) seconds / COUNTDOWN_TIME));
			});

			seconds--;
		});
//		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
//
//			@Override
//			public void run() {
//
//				if (seconds % 10 == 0) {
//					ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.GOLD + seconds
//							+ ChatColor.YELLOW + " Sekunden!");
//				}
//				if (seconds <= 5 && seconds > 0) {
//					if (seconds == 1) {
//						ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.GOLD + seconds
//								+ ChatColor.YELLOW + " Sekunde!");
//					} else {
//
//						ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.GOLD + seconds
//								+ ChatColor.YELLOW + " Sekunden!");
//					}
//					Bukkit.getOnlinePlayers().forEach(player -> {
//						ChatUtils.sendTitle(player, countdownColours[seconds - 1] + "" + seconds, "", 0, 20, 0);
//
//					});
//
//				}
//
//				if (seconds == 0) {
//
//					Bukkit.getOnlinePlayers().forEach(player -> {
//						player.getInventory().clear();
//						Ability[] abilities = championsManager.getSelectedChampion(player).getAbilities();
//						for (int i = 0; i < abilities.length; i++) {
//							player.getInventory().setItem(i, abilities[i].getIcon());
//						}
//
//						ItemStack autoAttackItem = championsManager.getSelectedChampion(player).getAutoAttackItem();
//
//						player.getInventory().setItem(4, autoAttackItem);
//						for (int i = 5; i < 9; i++) {
//							player.getInventory().setItem(i,
//									new ItemBuilder(new ItemStack(Material.BARRIER))
//											.setDisplayName(ChatColor.RED + "NA")
//											.setLore(ChatColor.GRAY + "This slot is not available!").toItemStack());
//						}
//						customPlayerManager.addPlayer(player);
//						player.getInventory().setHelmet(championsManager.getSelectedChampion(player).getIcon());
//
//					});
//
//					customPlayerManager.startBackgroundTask();
//					gameStateManager.setGameState(GameState.INGAME_STATE);
//					stop();
//				}
//
//				Bukkit.getOnlinePlayers().forEach(player -> {
//					player.setLevel(seconds);
//					player.setExp(((float) seconds / COUNTDOWN_TIME));
//				});
//
//				seconds--;
//			}
//		}, 0, 20);

	}

	@Override
	public void stop() {
		if (isRunning) {
			this.isRunning = false;
			bukkitTask.cancel();
			seconds = COUNTDOWN_TIME;
		}
	}

	public void startIdle() {
		this.isIdling = true;
		this.idleTask = TaskManager.getInstance().repeat(IDLE_TIME, IDLE_TIME, task -> {
			ChatUtils.broadCastMessage(ChatColor.YELLOW + "Bis zum Spielstart fehlen noch " + ChatColor.GOLD
					+ (LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getAlivePlayers().size())
					+ ChatColor.YELLOW + " Spieler!");
		});
	}

	public void stopIdle() {
		if (isIdling) {
			idleTask.cancel();
			this.isIdling = false;
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public boolean isIdling() {
		return isIdling;
	}

	/**
	 * Returns the remaining seconds of this countdown
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Sets the remaining countdown time
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
