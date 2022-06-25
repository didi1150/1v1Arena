package me.didi.champion.ability.impl.lloyd;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.champion.ability.Recastable;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ArmorStandFactory;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import me.didi.utilities.MathUtils;

public class LloydThirdAbility extends Recastable implements Ability {

	private Map<Player, ArmorStand> markerStands = new HashMap<Player, ArmorStand>();
	private Map<Player, Entity> markedEntities = new HashMap<Player, Entity>();
	private LloydThirdAbility instance;

	public LloydThirdAbility() {
		instance = this;
	}

	@Override
	public String getName() {
		return ChatColor.GREEN + "Shuriken Flip";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.NETHER_STAR)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Lloyd throws a shuriken, propelling himself backwards.",
				ChatColor.GRAY + "If the shuriken hits, the enemy takes " + ChatColor.DARK_AQUA + " 10 damage",
				ChatColor.GRAY + "and is marked for 3 seconds. During this time",
				ChatColor.GRAY + "Lloyd can recast this ability to dash into the target",
				ChatColor.GRAY + "dealing " + ChatColor.DARK_AQUA + " 25 damage " + ChatColor.GRAY + "on impact." };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MOVEMENT;
	}

	@Override
	public int getCooldown() {
		return 15;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		getRecasts()[recastCounters.getOrDefault(player, 0)].accept(player, abilityStateManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BiConsumer<Player, AbilityStateManager>[] getRecasts() {
		BiConsumer<Player, AbilityStateManager>[] consumers = new BiConsumer[2];

		consumers[0] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				flip(player, abilityStateManager, false);
			}

			private void flip(Player player, AbilityStateManager abilityStateManager, boolean isEnemyHit) {
				abilityStateManager.addCooldown(player, 2, getCooldown());
				ArmorStand armorStand = (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(player.getLocation());
				armorStand.setMarker(true);
				armorStand.setGravity(false);
				armorStand.setItemInHand(new ItemStack(Material.NETHER_STAR));

				Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(13));

				Vector armorVector = destination.clone().subtract(player.getLocation()).toVector().normalize();

				TaskManager.getInstance().repeat(0, 1, task -> {
					if (armorStand.getLocation().distanceSquared(destination) <= 2) {
						armorStand.remove();
						task.cancel();
					}

					for (Entity entity : armorStand.getNearbyEntities(2, 2, 2)) {
						if (DamageManager.isEnemy(player, entity)) {
							abilityStateManager.removeCooldown(player);
							abilityStateManager.addRecastCooldown(player, 2, getRecastCountdown());
							recastCounters.put(player, 1);
							DamageManager.damageEntity(player, entity, DamageReason.MAGIC, 10, false);
							armorStand.remove();
							task.cancel();
							markerStands.put(player, (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(
									MathUtils.getLocationToRight(entity.getLocation().add(0, 2.5, 0), 0.1)));
							markedEntities.put(player, entity);
							ArmorStand markerStand = markerStands.get(player);
							markerStand.setRightArmPose(
									new EulerAngle(Math.toRadians(90), Math.toRadians(90), Math.toRadians(10)));
							markerStand.setMarker(true);
							markerStand.setGravity(false);
							markerStand.setItemInHand(new ItemStack(Material.IRON_SWORD));

							TaskManager.getInstance().repeatUntil(0, 1, 20 * 3, (bukkitTask, counter) -> {
								markerStand.teleport(
										MathUtils.getLocationToRight(entity.getLocation().add(0, 2.5, 0), 0.1));

								if (counter.get() >= 20 * 3) {
									abilityStateManager.removeRecastCooldown(player, instance, 2);
									abilityStateManager.addCooldown(player, 2, getCooldown());
									markerStands.remove(player);
									markedEntities.remove(player);
									markerStand.remove();
									recastCounters.put(player, 0);
								}
							});
							break;
						}
					}

					armorStand.teleport(armorStand.getLocation().add(armorVector));

				});

				Vector vector = player.getLocation()
						.subtract(player.getLocation().add(player.getLocation().getDirection().multiply(6))).toVector()
						.setY(0);
				player.setVelocity(vector);

			}
		};

		consumers[1] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				fly(player, abilityStateManager);
			}

			private void fly(Player player, AbilityStateManager abilityStateManager) {
				Entity target = markedEntities.get(player);
				if (target == null)
					return;

				ArmorStand markedStand = markerStands.get(player);

				if (markedStand == null)
					return;

				markedStand.remove();
				Vector vector = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(3);
				TaskManager.getInstance().repeatUntil(0, 1, Long.MAX_VALUE, (task, counter) -> {

					if (player.getLocation().distanceSquared(target.getLocation()) <= 5) {
						abilityStateManager.addCooldown(player, 2, getCooldown());
						player.setVelocity(new Vector(0, 0, 0));
						task.cancel();

						DamageManager.damageEntity(player, target, DamageReason.MAGIC, 15, false);
						recastCounters.put(player, 0);
						return;
					}

					player.setVelocity(vector);
				});
			}
		};

		return consumers;
	}

	@Override
	public int getRecastCountdown() {
		return 3;
	}

}
