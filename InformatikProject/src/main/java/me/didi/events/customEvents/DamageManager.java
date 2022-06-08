package me.didi.events.customEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class DamageManager {

	public void damageEntity(Entity from, Entity target, DamageReason damageReason, double damage, boolean knockback) {
		Bukkit.getPluginManager().callEvent(new CustomDamageEvent(target, from, damageReason, damage, knockback));
	}

	public void knockbackEnemy(Entity from, Entity target) {
		if (target.isOnGround())
			target.setVelocity(from.getLocation().clone().getDirection().normalize().multiply(0.3).setY(0.3));
		else
			target.setVelocity(from.getLocation().clone().getDirection().normalize().multiply(0.3).setY(0.05));
	}

}
