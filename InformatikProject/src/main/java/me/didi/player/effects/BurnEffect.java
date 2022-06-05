package me.didi.player.effects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.events.damageSystem.CustomDamageEvent;
import me.didi.events.damageSystem.DamageReason;
import me.didi.utilities.ParticleUtils;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import xyz.xenondevs.particle.ParticleEffect;

public class BurnEffect extends SpecialEffect {

	public BurnEffect(Entity from, Entity to, double duration, double damagePerSec) {
		super(from, to, duration);
		burnEnemy(from, to, damagePerSec);
	}

	public void burnEnemy(Entity from, Entity to, double damagePerSec) {
		net.minecraft.server.v1_8_R3.Entity craftEntity = ((CraftEntity) to).getHandle();
		AxisAlignedBB bb = craftEntity.getBoundingBox();

		Location location = to.getLocation().clone();
		location.setY(bb.e);

		double radius = (bb.d - bb.a) / 2;
		new BukkitRunnable() {

			int counter = 0;

			@Override
			public void run() {

				if (counter >= duration) {
					endEffect();
					this.cancel();
				}

				Bukkit.getPluginManager()
						.callEvent(new CustomDamageEvent(to, from, DamageReason.MAGIC, damagePerSec, false));

				ParticleUtils.drawCircle(ParticleEffect.FLAME, null, location, radius);

				counter++;
			}
		}.runTaskTimer(MainClass.getPlugin(), 0, 20);
	}

	@Override
	public void handleEvent(Event event) {

	}

}
