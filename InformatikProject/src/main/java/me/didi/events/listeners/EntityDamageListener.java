package me.didi.events.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.CustomPlayerDeathEvent;
import me.didi.events.customEvents.CustomPlayerHealthChangeEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.gamesystem.GameStateManager;
import me.didi.gamesystem.gameStates.IngameState;
import me.didi.menus.ScoreboardHandler;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.TaskManager;
import me.didi.utilities.Utils;

public class EntityDamageListener implements Listener {

	private GameStateManager gameStateManager;
	private CurrentStatGetter currentStatGetter;

	private List<Player> attackCooldowns;

	public EntityDamageListener(GameStateManager gameStateManager, CurrentStatGetter currentStatGetter) {
		this.gameStateManager = gameStateManager;
		this.currentStatGetter = currentStatGetter;
		this.attackCooldowns = new ArrayList<Player>();
	}

	@EventHandler
	public void onTakeDamage(EntityDamageEvent event) {
		if (!(gameStateManager.getCurrentGameState() instanceof IngameState)) {
			event.setCancelled(true);
			return;
		}
		if (event.getEntity() instanceof Player) {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				ScoreboardHandler.getInstance().updatePlayerHealth(pl);
			}
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

		if (event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			ScoreboardHandler.getInstance().updatePlayerHealth(attacker);
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

	@EventHandler(priority = EventPriority.LOW)
	public void onDamageCalculate(CustomDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (event.getDamageReason() == DamageReason.AUTO) {
				Player attacker = (Player) event.getAttacker();
				float attackSpeed = CurrentStatGetter.getInstance().getAttackSpeed(attacker);
				float rounded = (float) Math.round(attackSpeed * 10);

				int ticks = (int) (20 / (rounded / 10));
				if (!attackCooldowns.contains(attacker)) {
					attackCooldowns.add(attacker);

					TaskManager.getInstance().runTaskLater(ticks, task -> {
						attackCooldowns.remove(attacker);
					});
				} else {
					event.setCancelled(true);
					return;
				}
			}

			double calculatedDamage = event.getDamage();

			if (currentStatGetter.getCustomPlayer(player) == null) {
				event.setCancelled(true);
				return;
			}

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

			event.setDamage(calculatedDamage);
		} else {
			LivingEntity ent = (LivingEntity) event.getEntity();
			ent.damage(event.getDamage());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onProcess(CustomDamageEvent event) {

		if (event.isCancelled())
			return;
		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();

			boolean knockback = event.isKnockback();

			double calculatedDamage = event.getDamage();

			CustomPlayer customPlayer = CustomPlayerManager.getInstance().getPlayer(player);

			if (customPlayer == null)
				return;

			Utils.spawnIndicator(event.getEntity().getLocation(), event.getDamageReason(), calculatedDamage);

			if (calculatedDamage < customPlayer.getRemainingShield()) {
				customPlayer.setRemainingShield((float) (customPlayer.getRemainingShield() - calculatedDamage));

			} else {
				calculatedDamage -= customPlayer.getRemainingShield();
				customPlayer.setRemainingShield(0);

				CustomPlayerHealthChangeEvent customPlayerHealthChangeEvent = new CustomPlayerHealthChangeEvent(
						customPlayer, customPlayer.getCurrentHealth(),
						customPlayer.getCurrentHealth() - (float) calculatedDamage);
				Bukkit.getPluginManager().callEvent(customPlayerHealthChangeEvent);
				if (customPlayerHealthChangeEvent.isCancelled()) {
					return;
				}

				if (customPlayer.getCurrentHealth() - calculatedDamage <= 0) {
					customPlayer.setCurrentHealth(customPlayer.getBaseHealth());
					Bukkit.getPluginManager().callEvent(new CustomPlayerDeathEvent(event.getAttacker(), player));
					return;
				}
				customPlayer.setCurrentHealth((float) (customPlayer.getCurrentHealth() - calculatedDamage));
			}

			player.damage(0);
			if (knockback)
				DamageManager.knockbackEnemy(event.getAttacker(), event.getEntity());

			ScoreboardHandler.getInstance().updatePlayerHealth((Player) event.getAttacker());
		}
	}

}
