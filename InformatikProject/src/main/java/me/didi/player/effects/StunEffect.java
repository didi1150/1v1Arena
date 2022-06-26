package me.didi.player.effects;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.didi.champion.ability.AbilityStateManager;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.utilities.Utils;
import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import xyz.xenondevs.particle.ParticleEffect;

public class StunEffect extends SpecialEffect {

	public StunEffect(Entity from, Entity to, double duration) {
		super(from, to, duration);
		display(to);
	}

	private void display(Entity entity) {
		if (to instanceof Player)
			AbilityStateManager.getInstance().disableAbilities((Player) to, Utils.round(duration));
		LivingEntity ent = (LivingEntity) to;
		ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 255, false, false));
		net.minecraft.server.v1_8_R3.Entity craftEntity = ((CraftEntity) to).getHandle();
		AxisAlignedBB bb = craftEntity.getBoundingBox();
		double radius = (bb.d - bb.a) / 2;
		TaskManager.getInstance().repeatUntil(1, 1, (long) (20 * duration), (task, counter) -> {

			Location location = ent.getLocation();

			Location highLocation = location.clone();
			highLocation.setY(bb.e);
			if (counter.get() >= duration * 20)
				endEffect();

			if (counter.get() % 3 == 0) {

				final int batAmount = 40;
				final double increments = 4 * Math.PI / batAmount; // the angle to increment so all bats are evenly
																	// spaced
																	// out

				double increase = 0.35 / batAmount;
				double startRadius = 0.03;
				for (double t = 0; t < 4 * Math.PI; t += increments) {

					double x = startRadius * Math.cos(t);
					double z = startRadius * Math.sin(t);

					Vector rotatedVector = new Vector(x, 0, z);
					double angleRadians = Math.toRadians(90);
					rotatedVector = Utils.rotateAroundAxisX(rotatedVector, Math.cos(angleRadians),
							Math.sin(angleRadians));

					float yaw = to.getLocation().getYaw();
					double yawRadians = -Math.toRadians(yaw);
					rotatedVector = Utils.rotateAroundAxisY(rotatedVector, Math.cos(yawRadians),
							Math.sin(yawRadians));
					Location loc = highLocation.add(rotatedVector);

					ParticleEffect.REDSTONE.display(loc, new Color(85, 0, 102));
					startRadius += increase;
				}
			}
		});
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
			if (playerMoveEvent.getPlayer() == to) {
				playerMoveEvent.setCancelled(true);
			}
		}

		if (event instanceof AbilityCastEvent) {
			((AbilityCastEvent) event).setCancelled(true);
		}
	}

}
