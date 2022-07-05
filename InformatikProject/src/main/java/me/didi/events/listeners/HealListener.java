package me.didi.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.events.customEvents.CustomPlayerHealthChangeEvent;
import me.didi.menus.ScoreboardHandler;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;

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
		for (Player pl : Bukkit.getOnlinePlayers()) {
			ScoreboardHandler.getInstance().updatePlayerHealth(pl);
		}

		int bonusHealth = CustomPlayerManager.getInstance().getBonusHealth(Bukkit.getPlayer(customPlayer.getUuid()));
		CustomPlayerHealthChangeEvent customPlayerHealthChangeEvent = new CustomPlayerHealthChangeEvent(customPlayer,
				customPlayer.getCurrentHealth(), customPlayer.getCurrentHealth() + bonusHealth);
		Bukkit.getPluginManager().callEvent(customPlayerHealthChangeEvent);

		if (customPlayerHealthChangeEvent.isCancelled())
			return;

		if (customPlayer.getCurrentHealth() + event.getHealAmount() < (customPlayer.getBaseHealth() + bonusHealth)) {
			customPlayer.setCurrentHealth(customPlayer.getCurrentHealth() + event.getHealAmount());
			return;
		} else {
			customPlayer.setCurrentHealth(customPlayer.getBaseHealth() + bonusHealth);
			return;
		}
	}

}
