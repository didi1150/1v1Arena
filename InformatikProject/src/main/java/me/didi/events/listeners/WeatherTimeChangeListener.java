package me.didi.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherTimeChangeListener implements Listener {

	@EventHandler
	public void onChange(WeatherChangeEvent event) {
		Bukkit.getWorlds().forEach(world -> {
			world.setTime(0);
		});
		event.setCancelled(true);
		
	}
}
