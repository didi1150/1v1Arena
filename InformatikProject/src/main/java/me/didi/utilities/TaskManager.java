package me.didi.utilities;

import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;

public class TaskManager {

	private static MainClass plugin;

	public TaskManager(MainClass plugin) {
		TaskManager.plugin = plugin;
	}

	public static BukkitTask runTaskTimerContinuously(long delay, long period) {
		return new BukkitRunnable() {

			@Override
			public void run() {

			}
		}.runTaskTimer(null, delay, period);
	}

	public static BukkitTask repeatUntil(long delay, long period, long duration, Consumer<Runnable> callback) {
		return new BukkitRunnable() {

			int counter = 0;

			@Override
			public void run() {

				if (counter >= duration) {
					this.cancel();
				}

				counter++;
			}
		}.runTaskTimer(plugin, delay, period);
	}

	public static BukkitTask repeat(long delay, long period, Consumer<Runnable> callback) {
		return new BukkitRunnable() {

			@Override
			public void run() {

			}
		}.runTaskTimer(plugin, delay, period);
	}

}
