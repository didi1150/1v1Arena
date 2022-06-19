package me.didi.player.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageReason;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import xyz.xenondevs.particle.ParticleEffect;

public class BurnEffect extends SpecialEffect {

	public BurnEffect(Entity from, Entity to, double duration, double damagePerSec) {
		super(from, to, duration);
		to.setFireTicks(to.getFireTicks() + (int) (20 * duration));
		burnEnemy(from, to, damagePerSec);
	}

	public void burnEnemy(Entity from, Entity to, double damagePerSec) {
		net.minecraft.server.v1_8_R3.Entity craftEntity = ((CraftEntity) to).getHandle();
		AxisAlignedBB bb = craftEntity.getBoundingBox();

		double radius = (bb.d - bb.a) / 2;

		TaskManager.getInstance().repeatUntil(0, 1, (long) duration * 20, (task, counter) -> {
			Location location = to.getLocation().clone();
			location.setY(bb.e);
			if (counter.get() >= duration * 20) {
				endEffect();
				return;
			}

			if (counter.get() % 20 == 0)
				Bukkit.getPluginManager()
						.callEvent(new CustomDamageEvent(to, from, DamageReason.MAGIC, damagePerSec, false));
			if (counter.get() % 3 == 0)
				ParticleUtils.drawCircle(ParticleEffect.FLAME, null, location, radius);
		});
	}

	@Override
	public void handleEvent(Event event) {

	}

}
