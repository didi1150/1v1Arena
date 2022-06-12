package me.didi.gamesystem.countdowns;

import org.bukkit.scheduler.BukkitTask;

public abstract class Countdown {

	protected int taskID;
	
	protected BukkitTask bukkitTask;

	public abstract void start();

	public abstract void stop();

}
