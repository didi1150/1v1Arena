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

import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import xyz.xenondevs.particle.ParticleEffect;

public class RootEffect extends SpecialEffect {

	public RootEffect(Entity from, Entity to, double duration) {
		super(from, to, duration);
		rootEnemy(to);
	}

	private void rootEnemy(Entity to) {
		LivingEntity ent = (LivingEntity) to;
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 255, false, false));
		net.minecraft.server.v1_8_R3.Entity craftEntity = ((CraftEntity) to).getHandle();

		Location location = ent.getLocation();
		AxisAlignedBB bb = craftEntity.getBoundingBox();

		Location highLocation = location.clone();
		highLocation.setY(bb.e);

		double radius = (bb.d - bb.a) / 2;
		TaskManager.getInstance().repeatUntil(1, 1, (long) (20 * duration), (task, counter) -> {
			if (counter.get() >= duration * 20)
				endEffect();

			ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.WHITE, highLocation, radius);
			ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.WHITE, ent.getLocation(), radius);

			for (double t = 0; t <= 2 * Math.PI * 10.5; t += 2 * Math.PI / 4) {
				double x = (radius * Math.cos(t));
				double z = (radius * Math.sin(t));

				ParticleUtils.drawVerticalLines(ParticleEffect.REDSTONE, Color.WHITE, location.clone().add(x, 0, z),
						bb.e - location.getY(), bb.e / 100);
			}
		});
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
