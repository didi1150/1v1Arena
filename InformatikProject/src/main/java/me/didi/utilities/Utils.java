package me.didi.utilities;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import xyz.xenondevs.particle.ParticleBuilder;

public class Utils {

	public static Location getLocationToRight(Location location, double distance) {

		final float newZ = (float) (location.getZ()
				+ (-distance * Math.sin(Math.toRadians(location.getYaw() + 90 * 0))));

		final float newX = (float) (location.getX()
				+ (-distance * Math.cos(Math.toRadians(location.getYaw() + 90 * 0))));

		return new Location(location.getWorld(), newX, location.getY(), newZ, location.getYaw(), location.getPitch());
	}

	public static Location getLocationToLeft(Location location, double distance) {

		// 0 left/right | 1 front/back
		final float newZ = (float) (location.getZ()
				+ (distance * Math.sin(Math.toRadians(location.getYaw() + 90 * 0))));

		final float newX = (float) (location.getX()
				+ (distance * Math.cos(Math.toRadians(location.getYaw() + 90 * 0))));

		return new Location(location.getWorld(), newX, location.getY(), newZ, location.getYaw(), location.getPitch());
	}

	public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
		// Gravity of a potion
		double gravity = 0.115;
		// Block locations
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));
		// Height gain
		int gain = heightGain;
		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
		// Solve quadratic equation for velocity
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;
		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
		// Vertical velocity
		double vy = Math.sqrt(maxGain * gravity);
		// Horizontal velocity
		double vh = vy / slope;
		// Calculate horizontal direction
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;
		// Horizontal velocity components
		double vx = vh * dirx;
		double vz = vh * dirz;
		return new Vector(vx, vy, vz);
	}

	private static double distanceSquared(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();
		return dx * dx + dz * dz;
	}

	/**
	 * Gets the highest location below the location where the block is not air
	 */
	public static Location getHighestLocation(Location location) {
		Location start = location;

		for (int y = start.getBlockY(); y > 0; y--) {
			start.setY(y);
			if (location.getWorld().getBlockAt(start).getType() != Material.AIR) {
				return start;
			}
		}
		return start;
	}

	public static Player getTargetPlayer(Player player, int max) {
		List<Player> possible = player.getNearbyEntities(max, max, max).stream()
				.filter(entity -> entity instanceof Player).map(entity -> (Player) entity)
				.filter(player::hasLineOfSight).collect(Collectors.toList());
		Ray ray = Ray.from(player);
		double d = -1;
		Player closest = null;
		for (Player player1 : possible) {
			double dis = AABB.from(player1).collidesD(ray, 0, max);
			if (dis != -1) {
				if (dis < d || d == -1) {
					d = dis;
					closest = player1;
				}
			}
		}
		return closest;
	}

	public static double getRandomBetween(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	public static void shootAutoAttackProjectile(Player player, double range, ItemStack heldItem, double damage,
			boolean knockback, double speed) {
		ArmorStand armorStand = (ArmorStand) ArmorStandFactory
				.spawnInvisibleArmorStand(getLocationToRight(player.getLocation().add(0, 0.4, 0), 0.3));
		armorStand.setMarker(true);
		armorStand.setArms(true);
		armorStand.setItemInHand(heldItem);
		armorStand.setGravity(false);

		Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(range));

		Vector vec = destination.clone().subtract(player.getLocation()).toVector();
		TaskManager.getInstance().repeat(1, 1, task -> {
			armorStand.teleport(armorStand.getLocation().add(vec.normalize().multiply(speed)));

			Block blockAt = armorStand.getWorld()
					.getBlockAt(getLocationToRight(armorStand.getLocation().add(0, 0.5, 0), 0.3));

			if (armorStand.getLocation().distanceSquared(destination) <= 1 || blockAt.getType().isSolid()) {

				armorStand.remove();
				task.cancel();
				return;
			}

			armorStand.getNearbyEntities(0.5, 1, 0.5).stream().filter(entity -> entity instanceof LivingEntity)
					.filter(entity -> !(entity instanceof ArmorStand)).filter(entity -> entity != player)
					.collect(Collectors.toList()).forEach(entity -> {
						DamageManager.damageEntity(player, entity, DamageReason.AUTO, damage, knockback);

						armorStand.remove();
						task.cancel();
						return;
					});
		});
	}

	public static void spawnIndicator(Location location, DamageReason damageReason, double damage) {
		ChatColor color = null;

		if (damageReason == DamageReason.AUTO || damageReason == DamageReason.PHYSICAL) {
			color = ChatColor.GOLD;
		} else if (damageReason == DamageReason.MAGIC) {
			color = ChatColor.AQUA;
		} else {
			color = ChatColor.WHITE;
		}

		Random random = new Random();
		double x = random.nextDouble() - 0.5;
		double z = random.nextDouble() - 0.5;

		ArmorStand armorStand = (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(location.add(x, 1, z));
		armorStand.setGravity(false);
		armorStand.setCustomNameVisible(true);

		armorStand.setCustomName(
				(damage >= 100 ? color + "" + ChatColor.BOLD : color) + new DecimalFormat("#").format(damage));
		armorStand.setMarker(true);
		TaskManager.getInstance().runTaskLater(15, task -> {
			armorStand.remove();
		});
	}

	public static void spawnIndicator(Location location, ChatColor color, double damage) {
		Random random = new Random();
		double x = random.nextDouble() - 0.5;
		double z = random.nextDouble() - 0.5;

		ArmorStand armorStand = (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(location.add(x, 1, z));
		armorStand.setGravity(false);
		armorStand.setCustomNameVisible(true);

		armorStand.setCustomName(
				(damage >= 100 ? color + "" + ChatColor.BOLD : color) + new DecimalFormat("#").format(damage));
		armorStand.setMarker(true);

		TaskManager.getInstance().runTaskLater(15, task -> {
			armorStand.remove();
		});
	}

	public static void shootProjectile(Player player, double range, ItemStack heldItem, double damage,
			boolean knockback, double speed, ParticleBuilder trail, DamageReason damageReason, Consumer<Entity> onHit) {
		ArmorStand armorStand = (ArmorStand) ArmorStandFactory
				.spawnInvisibleArmorStand(getLocationToRight(player.getLocation().add(0, 0.4, 0), 0.3));
		armorStand.setMarker(true);
		armorStand.setArms(true);
		armorStand.setItemInHand(heldItem);
		armorStand.setGravity(false);

		Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(range));

		Vector vec = destination.clone().subtract(player.getLocation()).toVector();
		TaskManager.getInstance().repeatUntil(1, 1, Long.MAX_VALUE, (task, counter) -> {
			armorStand.teleport(armorStand.getLocation().add(vec.normalize().multiply(speed)));
			Block blockAt = armorStand.getWorld()
					.getBlockAt(getLocationToRight(armorStand.getLocation().add(0, 0.5, 0), 0.3));

			if (armorStand.getLocation().distanceSquared(destination) <= 1 || blockAt.getType().isSolid()) {

				armorStand.remove();
				task.cancel();
				return;
			}

			if (trail != null && counter.get() >= 3)
				trail.setLocation(getLocationToRight(armorStand.getLocation().add(0, 0.75, 0), 0.45)
						.subtract(armorStand.getLocation().getDirection())).display();
			armorStand.getNearbyEntities(0.5, 1, 0.5).stream().filter(entity -> entity instanceof LivingEntity)
					.filter(entity -> !(entity instanceof ArmorStand)).filter(entity -> entity != player)
					.collect(Collectors.toList()).forEach(entity -> {
						DamageManager.damageEntity(player, entity, damageReason, damage, knockback);
						onHit.accept(entity);

						armorStand.remove();
						task.cancel();
						return;
					});
		});
	}

	public static int round(double d) {
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - (double) i;
		if (result < 0.5) {
			return d < 0 ? -i : i;
		} else {
			return d < 0 ? -(i + 1) : i + 1;
		}
	}

	/**
	 * Convert to radians first
	 * 
	 * @param cos insert angle in cos function
	 * @param sin insert angle in sin function
	 */
	public static Vector rotateAroundAxisX(Vector v, double cos, double sin) {
		double y = v.getY() * cos - v.getZ() * sin;
		double z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	/**
	 * Convert to radians first Also angle is negated => use -angle
	 * 
	 * @param cos insert -angle in cos function
	 * @param sin insert -angle in sin function
	 */
	public static Vector rotateAroundAxisY(Vector v, double cos, double sin) {
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	/**
	 * Convert to radians first
	 * 
	 * @param cos insert angle in cos function
	 * @param sin insert angle in sin function
	 */
	public static Vector rotateAroundAxisZ(Vector v, double cos, double sin) {
		double x = v.getX() * cos - v.getY() * sin;
		double y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);

	}

	/**
	 * Usage: Only for displaying a custom item's passive's remaining duration
	 */
	public static BukkitTask showEffectStatus(Player player, int slot, int seconds, int tickPeriod,
			ItemStack displayItem, ItemStack originalItem, AtomicLong sharedCounter) {
		player.getInventory().setItem(slot, displayItem);
		return TaskManager.getInstance().repeatUntil(0, tickPeriod, 20 * seconds,
				new BiConsumer<BukkitTask, AtomicLong>() {

					double percentage = 0;
					double maxPercentage = 1;

					@Override
					public void accept(BukkitTask task, AtomicLong counter) {
						counter.set(sharedCounter.getAndAdd(1));
						percentage = counter.get() * (maxPercentage / seconds / (20 / tickPeriod));
						if (counter.get() >= 20 * seconds) {
							task.cancel();
							player.getInventory().setItem(slot, originalItem);
							return;
						}

						short durability = (short) (displayItem.getType().getMaxDurability() * percentage);
						displayItem.setDurability(durability);
						player.getInventory().setItem(slot, displayItem);
					}
				});
	}

}
