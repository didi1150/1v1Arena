package me.didi.player.effects;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.utilities.ParticleUtils;
import xyz.xenondevs.particle.ParticleEffect;

public class RootEffect extends SpecialEffect {

	public RootEffect(Entity from, Entity to, double duration) {
		super(from, to, duration);
		rootEnemy(to);
	}

	private void rootEnemy(Entity to) {
		LivingEntity ent = (LivingEntity) to;

		Location location = ent.getLocation();
		new BukkitRunnable() {

			int counter = 0;
			double radius = 0.5;

			@Override
			public void run() {
				if (counter >= duration * 20) {
					endEffect();
					this.cancel();
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

	@Override
	public void handleEvent(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent moveEvent = (PlayerMoveEvent) event;
			if (moveEvent.getPlayer() == to) {
				moveEvent.setCancelled(true);
			}
		}
	}

}
