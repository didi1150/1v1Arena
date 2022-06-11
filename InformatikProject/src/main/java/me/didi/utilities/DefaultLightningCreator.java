package me.didi.utilities;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

public class DefaultLightningCreator {
	public static Collection<Lightning> createLightning(World world, Location start, Vector dir, double length,
			double lengthMod, int maxChain, int maxLightnings, double maxJitter, double jitterMod, double splitChance,
			double splitMod) {
		if (maxChain <= 0) {
			return Collections.emptyList();
		}
		List<Lightning> lightnings = Lists.newArrayList();
//Plane plane = Plane.fromVector(dir.clone());

		boolean genFailed;
		do {
			genFailed = false;
//Vector lightningEnd = plane.randomPoint(start.toVector().add(dir.clone().normalize().multiply(length)), maxJitter);
			Random r = ThreadLocalRandom.current();
			Vector lightningEnd = start.clone().toVector().add(dir.clone().multiply(length))
					.add(new Vector((r.nextDouble() - 0.5), (r.nextDouble() - 0.5), (r.nextDouble() - 0.5)).normalize()
							.multiply((r.nextDouble() - 0.5) * maxJitter));
			Location locationEnd = lightningEnd.toLocation(world);
			if (locationEnd.distance(start) <= length) {
				genFailed = true;
				continue;
			}
			Lightning lightning = new Lightning(start, locationEnd.clone());
			lightnings.add(lightning);
			int currentLightningCount = Lightning.countTotalLightnings(lightnings);
			lightning.getAdjacentLightnings()
					.addAll(createLightning(world, locationEnd.clone(), dir.clone(), length * lengthMod, lengthMod,
							maxChain - 1, maxLightnings - currentLightningCount, maxJitter * jitterMod, jitterMod,
							splitChance * splitMod, splitMod));
			if (currentLightningCount >= maxLightnings) {
				break;
			}
		} while (ThreadLocalRandom.current().nextDouble() <= splitChance || genFailed);

		return lightnings;

	}

}
