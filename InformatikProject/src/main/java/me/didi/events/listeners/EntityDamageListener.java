package me.didi.events.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.didi.MainClass;
import me.didi.player.CustomPlayer;

public class EntityDamageListener implements Listener {

	private MainClass plugin;

	public EntityDamageListener(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTakeDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			event.setCancelled(true);
			Player player = (Player) event.getEntity();
			

			double damage = event.getDamage();
			CustomPlayer customPlayer = plugin.getCustomPlayerManager().getPlayer(player.getUniqueId());
			customPlayer.setCurrentHealth((int) (customPlayer.getCurrentHealth() - damage));
		}
	}

}
