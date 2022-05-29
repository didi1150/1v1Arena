package me.didi.gamesystem.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.didi.gamesystem.GameStateManager;
import me.didi.utilities.ChatUtils;

public class EndingCountdown extends Countdown {

	private GameStateManager gameStateManager;
	private static int COUNTDOWN_TIME = 15;
	private int seconds;

	public EndingCountdown(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		this.seconds = COUNTDOWN_TIME;
	}

	@Override
	public void start() {
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {

			@Override
			public void run() {

				if (seconds > 0) {
					Bukkit.getOnlinePlayers().forEach(player -> {
						player.setLevel(seconds);
						player.setExp(((float) seconds / COUNTDOWN_TIME));
					});

					if (seconds <= 5 && seconds > 0) {
						if (seconds == 1) {
							ChatUtils.broadCastMessage(ChatColor.YELLOW + "Der Server stoppt neu in " + ChatColor.GOLD
									+ seconds + ChatColor.YELLOW + " Sekunde!");
						} else {

							ChatUtils.broadCastMessage(ChatColor.YELLOW + "Der Server stoppt neu in " + ChatColor.GOLD
									+ seconds + ChatColor.YELLOW + " Sekunden!");
						}
					}
					if (seconds == 0)
						stop();
				}

				seconds--;
			}
		}, 20, 20);
	}

	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(taskID);
		Bukkit.getServer().shutdown();
	}

}
