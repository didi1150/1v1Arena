package me.didi.player.effects;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.events.damageSystem.CustomDamageEvent;
import me.didi.events.damageSystem.DamageReason;
import xyz.xenondevs.particle.ParticleEffect;

public class BurnEffect extends SpecialEffect {


	public BurnEffect(Entity from, Entity to, Consumer<Entity> callback, String eventName) {
		super(from, to, callback, eventName);
	}

	public static void burnEnemy(Entity from, Entity to, int duration, int damagePerSec) {
		new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {

				if (counter >= duration) {
					this.cancel();
				}

				Bukkit.getPluginManager()
						.callEvent(new CustomDamageEvent(to, from, DamageReason.MAGIC, damagePerSec, false));
				
				ParticleEffect.FLAME.display(to.getLocation());

				counter++;
			}
		}.runTaskTimer(MainClass.getPlugin(), 0, 20);
	}

}
