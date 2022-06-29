package me.didi.gamesystem.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.didi.gamesystem.GameState;
import me.didi.gamesystem.GameStateManager;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.TaskManager;

public class ItemSelectCountdown extends Countdown {

	private GameStateManager gameStateManager;
	private static final int COUNTDOWN_TIME = 20;

	private int seconds;

	public ItemSelectCountdown(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		this.seconds = COUNTDOWN_TIME;
	}

	@Override
	public void start() {
		bukkitTask = TaskManager.getInstance().repeatUntil(20, 20, 20 * COUNTDOWN_TIME, (task, counter) -> {
			if (seconds % 10 == 0) {
				ChatUtils.broadCastMessage(ChatColor.YELLOW + "Die item select phase stoppt in " + ChatColor.GOLD
						+ seconds + ChatColor.YELLOW + " Sekunden!");
			}

			if (seconds <= 5 && seconds > 0) {
				if (seconds == 1) {
					ChatUtils.broadCastMessage(ChatColor.YELLOW + "Das item select phase stoppt in " + ChatColor.GOLD
							+ seconds + ChatColor.YELLOW + " Sekunde!");
				} else {

					ChatUtils.broadCastMessage(ChatColor.YELLOW + "Die item select phase stoppt in " + ChatColor.GOLD
							+ seconds + ChatColor.YELLOW + " Sekunden!");
				}
			}

			Bukkit.getOnlinePlayers().forEach(player -> {
				player.setLevel(seconds);
				player.setExp(((float) seconds / COUNTDOWN_TIME));
			});

			if (seconds == 0)
				stop();

			seconds--;
		});
	}

	@Override
	public void stop() {
		if (bukkitTask != null) {
			bukkitTask.cancel();
			gameStateManager.setGameState(GameState.INGAME_STATE);
		}
	}

	public int getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
}
