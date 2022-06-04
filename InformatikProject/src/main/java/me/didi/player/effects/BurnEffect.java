package me.didi.player.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.events.damageSystem.CustomDamageEvent;
import me.didi.events.damageSystem.DamageReason;
import xyz.xenondevs.particle.ParticleEffect;

public class BurnEffect extends SpecialEffect {

	public BurnEffect(Entity from, Entity to, double duration, double damagePerSec) {
		super(from, to, duration);
		burnEnemy(from, to, damagePerSec);
	}

	public void burnEnemy(Entity from, Entity to, double damagePerSec) {
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

				ParticleEffect.FLAME.display(to.getLocation());

				counter++;
			}
		}.runTaskTimer(MainClass.getPlugin(), 0, 20);
	}

	@Override
	public void handleEvent(Event event) {

	}

}
