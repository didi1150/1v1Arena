package me.didi.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.didi.MainClass;
import me.didi.events.damageSystem.CustomDamageEvent;
import me.didi.events.damageSystem.CustomPlayerDeathEvent;
import me.didi.events.damageSystem.DamageReason;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.player.CustomPlayer;

public class EntityDamageListener implements Listener {

	private MainClass plugin;

	public EntityDamageListener(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTakeDamage(EntityDamageEvent event) {
		if (!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
			event.setCancelled(true);
			return;
		}
		if (event.getEntity() instanceof Player) {
			if (event.getCause() == DamageCause.ENTITY_ATTACK)
				return;
			if (event.getCause() == DamageCause.CUSTOM)
				return;
			event.setCancelled(true);
			Player player = (Player) event.getEntity();

			double damage = event.getDamage();
			CustomPlayer customPlayer = plugin.getCustomPlayerManager().getPlayer(player.getUniqueId());

			if (customPlayer.getCurrentHealth() - damage <= 0) {
				customPlayer.setCurrentHealth(customPlayer.getBaseHealth());
				Bukkit.getPluginManager().callEvent(new CustomPlayerDeathEvent(null, player));
				return;
			}

			customPlayer.setCurrentHealth((int) (customPlayer.getCurrentHealth() - damage));
			player.damage(0);
		}
	}

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent event) {
		if (!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
			event.setCancelled(true);
			return;
		}
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (event.getDamager() instanceof Player) {

				Player attacker = (Player) event.getDamager();
				event.setCancelled(true);
				if (attacker.getInventory().getHeldItemSlot() != 4)
					return;

				plugin.getDamageManager().damageEntity(attacker, player, DamageReason.AUTO,
						plugin.getCustomPlayerManager().getDamage(attacker), true);
			} else {
				plugin.getDamageManager().damageEntity(event.getDamager(), player, DamageReason.AUTO, event.getDamage(),
						true);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDamage(CustomDamageEvent event) {

		boolean knockback = event.isKnockback();
		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();

			double calculatedDamage = event.getDamage();
			CustomPlayer customPlayer = plugin.getCustomPlayerManager().getPlayer(player.getUniqueId());

			if (customPlayer == null)
				return;

			double damage = event.getDamage();

			if (event.getDamageReason() == DamageReason.PHYSICAL || event.getDamageReason() == DamageReason.AUTO) {
				float defense = customPlayer.getBaseDefense() + plugin.getCustomPlayerManager().getBonusDefense(player);
				if (defense >= 0) {
					calculatedDamage = (100 / (100 + defense)) * damage;
				} else {
					calculatedDamage = (2 - (100 / (100 - defense)));
				}
			} else if (event.getDamageReason() == DamageReason.MAGIC) {
				float magicResistance = customPlayer.getMagicResist()
						+ plugin.getCustomPlayerManager().getBonusMagicResistance(player);
				if (magicResistance >= 0) {
					calculatedDamage = (100 / (100 + magicResistance)) * damage;
				} else {
					calculatedDamage = (2 - (100 / (100 - magicResistance)));
				}

			}

			if (customPlayer.getCurrentHealth() - calculatedDamage <= 0) {
				customPlayer.setCurrentHealth(customPlayer.getBaseHealth());
				Bukkit.getPluginManager().callEvent(new CustomPlayerDeathEvent(event.getAttacker(), player));
				return;
			}
			customPlayer.setCurrentHealth((float) (customPlayer.getCurrentHealth() - calculatedDamage));

			player.damage(0);
		} else {
			LivingEntity ent = (LivingEntity) event.getEntity();
			ent.damage(event.getDamage());
		}
		if (knockback)
			plugin.getDamageManager().knockbackEnemy(event.getAttacker(), event.getEntity());
	}

}
