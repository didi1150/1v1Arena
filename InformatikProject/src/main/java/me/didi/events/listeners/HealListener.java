package me.didi.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.player.CustomPlayer;

public class HealListener implements Listener {

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onNaturalHealthRegen(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCustomHeal(CustomPlayerHealEvent event) {
		CustomPlayer customPlayer = event.getCustomPlayer();
		customPlayer.setCurrentHealth(customPlayer.getCurrentHealth() + event.getHealAmount());
	}

}
