package me.didi.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

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
			if (event.getCause() == DamageCause.ENTITY_ATTACK)
				return;
			event.setCancelled(true);
			Player player = (Player) event.getEntity();

			double damage = event.getDamage();
			CustomPlayer customPlayer = plugin.getCustomPlayerManager().getPlayer(player.getUniqueId());
			customPlayer.setCurrentHealth((int) (customPlayer.getCurrentHealth() - damage));
			player.damage(0);
		}
	}

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			event.setCancelled(true);

			double damage = event.getDamage();
			CustomPlayer customPlayer = plugin.getCustomPlayerManager().getPlayer(player.getUniqueId());
			customPlayer.setCurrentHealth((int) (customPlayer.getCurrentHealth() - damage));

			player.damage(0);
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					player.setVelocity(event.getDamager().getLocation().getDirection().normalize().multiply(0.3).setY(0.3));
				}
			}, 1);
		}
	}

}
