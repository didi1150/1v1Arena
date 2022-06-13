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

import me.didi.champion.ability.AbilityStateManager;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.utilities.MathUtils;
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
		AbilityStateManager.getInstance().disableAbilities((Player) to, MathUtils.round(duration));

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

			ParticleUtils.drawCircle(ParticleEffect.REDSTONE, new Color(85, 0, 102),
					highLocation.clone().add(0, 0.5, 0), radius);
			ParticleUtils.drawCircle(ParticleEffect.REDSTONE, new Color(85, 0, 102),
					highLocation.clone().add(0, 0.25, 0), radius);
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
