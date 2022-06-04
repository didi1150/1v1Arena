package me.didi.player.effects;

import java.awt.Color;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.utilities.ParticleUtils;
import xyz.xenondevs.particle.ParticleEffect;

public class RootEffect extends SpecialEffect {

	public RootEffect(Entity from, Entity to, Consumer<Entity> callback, String eventName) {
		super(from, to, callback, eventName);
	}

	public static void rootEnemy(Entity to, int duration) {
		LivingEntity ent = (LivingEntity) to;

		Location location = ent.getLocation();
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false), true);
		new BukkitRunnable() {

			int counter = 0;
			double radius = 0.5;

			@Override
			public void run() {
				if (counter >= duration * 20) {
					this.cancel();
					ent.removePotionEffect(PotionEffectType.SLOW);
				}
				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.WHITE, ent.getEyeLocation().add(0, 0.3, 0),
						radius);
				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.WHITE, ent.getLocation(), radius);

				for (double t = 0; t <= 2 * Math.PI * 10.5; t += 2 * Math.PI / 4) {
					double x = (radius * Math.cos(t));
					double z = (radius * Math.sin(t));

					ParticleUtils.drawVerticalLines(ParticleEffect.REDSTONE, Color.WHITE, location.clone().add(x, 0, z),
							2, 0.25);
				}
				counter++;
			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);
	}

}
