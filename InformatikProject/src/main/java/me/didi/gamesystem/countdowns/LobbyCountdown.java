package me.didi.gamesystem.countdowns;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.Champion;
import me.didi.champion.ChampionsManager;
import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.LobbyState;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ConfigHandler;
import me.didi.utilities.TaskManager;

public class LobbyCountdown extends Countdown {

	private static final int COUNTDOWN_TIME = 20, IDLE_TIME = 20 * 15;
	private BukkitTask idleTask;
	private GameStateManager gameStateManager;
	private boolean isIdling;

	private ChampionsManager championsManager;

	private ChatColor[] countdownColours = new ChatColor[] { ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
			ChatColor.GREEN, ChatColor.AQUA };

	private int seconds;
	private boolean isRunning;

	public LobbyCountdown(GameStateManager gameStateManager, ChampionsManager championsManager,
			ConfigHandler configHandler) {
		this.gameStateManager = gameStateManager;
		this.seconds = COUNTDOWN_TIME;
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
			if (seconds == 5) {
				Bukkit.getOnlinePlayers().forEach(player -> {
					player.playSound(player.getLocation(), Sound.ANVIL_LAND, 10, 1);

					Champion selectedChampion = championsManager.getSelectedChampion(player);

					if (selectedChampion == null) {
						Set<Champion> championSet = new HashSet<Champion>(championsManager.getSelectableChampions());
						selectedChampion = championSet.stream().skip(new Random().nextInt(championSet.size()))
								.findFirst().orElse(null);

						championsManager.setSelectedChampion(player.getUniqueId(), selectedChampion);
						ChatUtils.sendMessageToPlayer(player, ChatColor.YELLOW + "Du hast den Champion "
								+ selectedChampion.getName() + ChatColor.YELLOW + " ausgewählt.");
					}

					player.getInventory().clear();

				});
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

			if (seconds == 0)
				stop();

			Bukkit.getOnlinePlayers().forEach(player -> {
				player.setLevel(seconds);
				player.setExp(((float) seconds / COUNTDOWN_TIME));
			});

			seconds--;
		});
	}

	@Override
	public void stop() {
		if (isRunning) {
			this.isRunning = false;
			bukkitTask.cancel();
			seconds = COUNTDOWN_TIME;
			gameStateManager.setGameState(GameState.ITEM_SELECT_STATE);
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
