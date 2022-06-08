package me.didi.gamesystem.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.didi.champion.ChampionsManager;
import me.didi.gamesystem.GameStateManager;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ChatUtils;

public class EndingCountdown extends Countdown {

	private GameStateManager gameStateManager;
	private static int COUNTDOWN_TIME = 15;
	private int seconds;

	private CustomPlayerManager customPlayerManager;

	public EndingCountdown(GameStateManager gameStateManager, CustomPlayerManager customPlayerManager) {
		this.gameStateManager = gameStateManager;
		this.seconds = COUNTDOWN_TIME;
		this.customPlayerManager = customPlayerManager;
	}

	@Override
	public void start() {
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (seconds == 0)
					stop();
				if (seconds > 0) {
					Bukkit.getOnlinePlayers().forEach(player -> {
						player.setLevel(seconds);
						player.setExp(((float) seconds / COUNTDOWN_TIME));
					});

					if (seconds <= 5 && seconds > 0) {
						if (seconds == 1) {
							ChatUtils.broadCastMessage(ChatColor.YELLOW + "Der Server stoppt in " + ChatColor.GOLD
									+ seconds + ChatColor.YELLOW + " Sekunde!");
						} else {

							ChatUtils.broadCastMessage(ChatColor.YELLOW + "Der Server stoppt in " + ChatColor.GOLD
									+ seconds + ChatColor.YELLOW + " Sekunden!");
						}
					}

				}

				seconds--;
			}
		}, 20, 20);
	}

	@Override
	public void stop() {
		gameStateManager.getPlugin().getAlivePlayers().forEach(uuid -> {
			customPlayerManager.removePlayer(uuid);
		});
		Bukkit.getOnlinePlayers().forEach(player -> {
			player.kickPlayer("Server stop");
		});
		Bukkit.getScheduler().cancelTask(taskID);
		Bukkit.getServer().shutdown();
	}

}
