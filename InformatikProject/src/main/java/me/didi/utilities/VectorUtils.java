package me.didi.utilities;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VectorUtils {

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

}
