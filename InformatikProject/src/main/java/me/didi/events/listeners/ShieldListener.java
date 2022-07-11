package me.didi.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.didi.events.customEvents.CustomShieldCastEvent;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;

public class ShieldListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onShield(CustomShieldCastEvent event) {

		CustomPlayer customPlayer = CustomPlayerManager.getInstance().getPlayer(event.getTarget());
		customPlayer.setRemainingShield((float) (customPlayer.getRemainingShield() + event.getShieldAmount()));
	}

}
