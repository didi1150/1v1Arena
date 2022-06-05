package me.didi.player.effects;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.utilities.ParticleUtils;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import xyz.xenondevs.particle.ParticleEffect;

public class RootEffect extends SpecialEffect {

	public RootEffect(Entity from, Entity to, double duration) {
		super(from, to, duration);
		rootEnemy(to);
	}

	private void rootEnemy(Entity to) {
		LivingEntity ent = (LivingEntity) to;
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false));
		net.minecraft.server.v1_8_R3.Entity craftEntity = ((CraftEntity) to).getHandle();

		Location location = ent.getLocation();
		AxisAlignedBB bb = craftEntity.getBoundingBox();

		Location highLocation = location.clone();
		highLocation.setY(bb.e);
		new BukkitRunnable() {
			int counter = 0;
			double radius = (bb.d - bb.a) / 2;

			@Override
			public void run() {
				if (counter >= duration * 20) {
					ent.removePotionEffect(PotionEffectType.SLOW);
					endEffect();
					this.cancel();
				}

				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.WHITE, highLocation, radius);
				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.WHITE, ent.getLocation(), radius);

				for (double t = 0; t <= 2 * Math.PI * 10.5; t += 2 * Math.PI / 4) {
					double x = (radius * Math.cos(t));
					double z = (radius * Math.sin(t));

					ParticleUtils.drawVerticalLines(ParticleEffect.REDSTONE, Color.WHITE, location.clone().add(x, 0, z),
							bb.e - location.getY(), bb.e / 100);
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
