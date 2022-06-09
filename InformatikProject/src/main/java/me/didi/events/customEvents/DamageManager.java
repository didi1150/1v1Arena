package me.didi.events.customEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class DamageManager {

	public static void damageEntity(Entity from, Entity target, DamageReason damageReason, double damage,
			boolean knockback) {
		Bukkit.getPluginManager().callEvent(new CustomDamageEvent(target, from, damageReason, damage, knockback));
	}

	public static void knockbackEnemy(Entity from, Entity target) {
		if (target.isOnGround())
			target.setVelocity(from.getLocation().clone().getDirection().normalize().multiply(0.3).setY(0.3));
		else
			target.setVelocity(from.getLocation().clone().getDirection().normalize().multiply(0.3).setY(0.05));
	}

	public static boolean isEnemy(Entity from, Entity target) {
		if (target == from)
			return false;
		if (target instanceof LivingEntity && !(target instanceof ArmorStand))
			return true;
		return false;
	}

}
