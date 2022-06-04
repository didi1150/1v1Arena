package me.didi.utilities;

import java.awt.Color;

import org.bukkit.Location;

import xyz.xenondevs.particle.ParticleEffect;

public class ParticleUtils {

	public static void drawCircle(ParticleEffect particleEffect, Color color, Location centre, double radius) {
		for (double t = 0; t <= 2 * Math.PI * radius; t += 0.05) {
			double x = radius * Math.cos(t) + centre.getX();
			double z = centre.getZ() + radius * Math.sin(t);
			if (color == null) {
				particleEffect.display(centre.clone().add(x, 0, z));
			} else
				particleEffect.display(centre.clone().add(x, 0, z), color);
		}
	}

	public static void createSphere(ParticleEffect particleEffect, Color color, Location centre, double radius) {
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double defRadius = Math.sin(i);
			double y = Math.cos(i) * radius;
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				double x = Math.cos(a) * defRadius * radius;
				double z = Math.sin(a) * defRadius * radius;

				if (color == null) {
					particleEffect.display(centre.clone().add(x, y, z));

				} else
					particleEffect.display(centre.clone().add(x, y, z), color);
			}
		}

	}

	public static void drawVerticalLines(ParticleEffect particleEffect, Color color, Location start, double maxHeight,
			double summand) {
		for (double y = 1; y <= maxHeight; y += summand) {
			if (color == null) {
				particleEffect.display(start.clone().add(0, y, 0));
			} else
				particleEffect.display(start.clone().add(0, y, 0), color);
		}
	}

}
