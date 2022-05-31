package me.didi.utilities;

import org.bukkit.Location;

public class VectorUtils {

	public static Location getLocationToRight(Location location, double distance) {

		final float newZ = (float) (location.getZ()
				+ (-distance * Math.sin(Math.toRadians(location.getYaw() + 90 * 0))));

		final float newX = (float) (location.getX()
				+ (-distance * Math.cos(Math.toRadians(location.getYaw() + 90 * 0))));

		return new Location(location.getWorld(), newX, location.getY(), newZ);
	}

	public static Location getLocationToLeft(Location location, double distance) {

		// 0 left/right | 1 front/back
		final float newZ = (float) (location.getZ()
				+ (distance * Math.sin(Math.toRadians(location.getYaw() + 90 * 0))));

		final float newX = (float) (location.getX()
				+ (distance * Math.cos(Math.toRadians(location.getYaw() + 90 * 0))));

		return new Location(location.getWorld(), newX, location.getY(), newZ);
	}

}
