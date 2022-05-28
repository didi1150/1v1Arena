package me.didi.gamesystem.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;

public class LobbyCountdown extends Countdown {

	private static final int COUNTDOWN_TIME = 20, IDLE_TIME = 20 * 15;
	private int idleID;
	private GameStateManager gameStateManager;
	private boolean isIdling;

	private ChatColor[] countdownColours = new ChatColor[] { ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
			ChatColor.GREEN, ChatColor.AQUA };

	private int seconds;
	private boolean isRunning;

	public LobbyCountdown(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		this.seconds = COUNTDOWN_TIME;
	}

	@Override
	public void start() {
		this.isRunning = true;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {

			@Override
			public void run() {

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

					Bukkit.getOnlinePlayers().forEach(player -> {
						player.getInventory().clear();
						Ability[] abilities = gameStateManager.getPlugin().getChampionsManager()
								.getSelectedChampion(player).getAbilities();
						for (int i = 0; i < abilities.length; i++) {
							player.getInventory().setItem(i, abilities[i].getIcon());
						}

						ItemStack autoAttackItem = gameStateManager.getPlugin().getChampionsManager()
								.getSelectedChampion(player).getAutoAttackItem();

						gameStateManager.getPlugin().getCustomPlayerManager().addPlayer(player);

					});

					gameStateManager.getPlugin().getCustomPlayerManager().startBackgroundTask();
					gameStateManager.setGameState(GameState.INGAME_STATE);
					stop();
				}

				Bukkit.getOnlinePlayers().forEach(player -> {
					player.setLevel(seconds);
					player.setExp(((float) seconds / 20));
				});

				seconds--;
			}
		}, 0, 20);

	}

	@Override
	public void stop() {
		if (isRunning) {
			this.isRunning = false;
			Bukkit.getScheduler().cancelTask(taskID);
			seconds = COUNTDOWN_TIME;
		}
	}

	public void startIdle() {
		this.isIdling = true;
		this.idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {

			@Override
			public void run() {
				ChatUtils.broadCastMessage(ChatColor.YELLOW + "Bis zum Spielstart fehlen noch " + ChatColor.GOLD
						+ (LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getAlivePlayers().size())
						+ ChatColor.YELLOW + " Spieler!");
			}
		}, IDLE_TIME, IDLE_TIME);
	}

	public void stopIdle() {
		if (isIdling) {
			Bukkit.getScheduler().cancelTask(idleID);
			this.isIdling = false;
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public boolean isIdling() {
		return isIdling;
	}

}
