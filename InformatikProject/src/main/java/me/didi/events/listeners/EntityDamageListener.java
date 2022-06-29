package me.didi.events.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.CustomPlayerDeathEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.menus.ScoreboardHandler;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.utilities.Utils;

public class EntityDamageListener implements Listener {

	private GameStateManager gameStateManager;
	private CurrentStatGetter currentStatGetter;

	public EntityDamageListener(GameStateManager gameStateManager, CurrentStatGetter currentStatGetter) {
		this.gameStateManager = gameStateManager;
		this.currentStatGetter = currentStatGetter;
	}

	@EventHandler
	public void onTakeDamage(EntityDamageEvent event) {
		if (!(gameStateManager.getCurrentGameState() instanceof IngameState)) {
			event.setCancelled(true);
			return;
		}
		if (event.getEntity() instanceof Player) {
			Bukkit.getOnlinePlayers().forEach(pl -> {
				ScoreboardHandler.getInstance().updateOpponentHealth(pl);
			});

			if (event.getCause() == DamageCause.FIRE_TICK) {
				event.setCancelled(true);
				return;
			}
			if (event.getCause() == DamageCause.ENTITY_ATTACK)
				return;
			if (event.getCause() == DamageCause.CUSTOM)
				return;
			event.setCancelled(true);
			Player player = (Player) event.getEntity();

			double damage = event.getDamage();
			CustomPlayer customPlayer;
			if ((customPlayer = currentStatGetter.getCustomPlayer(player)) != null) {

				if (customPlayer.getCurrentHealth() - damage <= 0) {
					customPlayer.setCurrentHealth(customPlayer.getBaseHealth());
					Bukkit.getPluginManager().callEvent(new CustomPlayerDeathEvent(null, player));
					return;
				}

				customPlayer.setCurrentHealth((int) (customPlayer.getCurrentHealth() - damage));
				player.damage(0);
			}
		}
	}

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent event) {
		if (!(gameStateManager.getCurrentGameState() instanceof IngameState)) {
			event.setCancelled(true);
			return;
		}
		Bukkit.getOnlinePlayers().forEach(pl -> {
			ScoreboardHandler.getInstance().updateOpponentHealth(pl);
		});

		if (event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			event.setCancelled(true);
			if (attacker.getInventory().getHeldItemSlot() == 4)
				DamageManager.damageEntity(attacker, event.getEntity(), DamageReason.AUTO,
						currentStatGetter.getAttackDamage(attacker), true);
		} else {
			DamageManager.damageEntity(event.getDamager(), event.getEntity(), DamageReason.AUTO, event.getDamage(),
					true);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(CustomDamageEvent event) {

		boolean knockback = event.isKnockback();
		if (event.getEntity() instanceof Player) {
			Bukkit.getOnlinePlayers().forEach(pl -> {
				ScoreboardHandler.getInstance().updateOpponentHealth(pl);
			});
			Player player = (Player) event.getEntity();

			float attackSpeed = CurrentStatGetter.getInstance().getAttackSpeed((Player) event.getAttacker());
			float rounded = (float) Math.round(attackSpeed * 10);

			player.setMaximumNoDamageTicks(20 * (int) (rounded) / 10);

			double calculatedDamage = event.getDamage();
			CustomPlayer customPlayer;

			if ((customPlayer = currentStatGetter.getCustomPlayer(player)) == null)
				return;

			double damage = event.getDamage();

			if (event.getDamageReason() == DamageReason.PHYSICAL || event.getDamageReason() == DamageReason.AUTO) {
				float defense = currentStatGetter.getCurrentArmor(player);
				if (defense >= 0) {
					calculatedDamage = (100 / (100 + defense)) * damage;
				} else {
					calculatedDamage = (2 - (100 / (100 - defense)));
				}
			} else if (event.getDamageReason() == DamageReason.MAGIC) {
				float magicResistance = currentStatGetter.getCurrentMagicResistance(player);
				if (magicResistance >= 0) {
					calculatedDamage = (100 / (100 + magicResistance)) * damage;
				} else {
					calculatedDamage = (2 - (100 / (100 - magicResistance)));
				}

			}

			Utils.spawnIndicator(event.getEntity().getLocation(), event.getDamageReason(), calculatedDamage);

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
			DamageManager.knockbackEnemy(event.getAttacker(), event.getEntity());
	}

}
