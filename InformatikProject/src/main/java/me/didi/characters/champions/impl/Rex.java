package me.didi.characters.champions.impl;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.RangedChampion;
import me.didi.events.damageSystem.CustomDamageEvent;
import me.didi.events.damageSystem.DamageReason;
import me.didi.utilities.ChatUtils;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class Rex extends RangedChampion {

	private boolean isOnCooldown;

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
		isOnCooldown = false;
	}

	@Override
	public Champion clone() {
		return new Rex(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon(),
				getAutoAttackItem());
	}

	@Override
	public void executeAutoAttack() {
		if (isOnCooldown)
			return;
		else {
			isOnCooldown = true;
			Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

				@Override
				public void run() {
					isOnCooldown = false;
				}
			}, 20 / 4);
			ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(),
					EntityType.ARMOR_STAND);
			armorStand.setArms(true);
			armorStand.setGravity(false);
			armorStand.setBasePlate(false);
			armorStand.setVisible(false);
			armorStand.setItemInHand(new ItemStack(Material.PRISMARINE_CRYSTALS));
			armorStand.setMarker(true);
			Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(10));
			Vector vec = destination.subtract(player.getLocation()).toVector();

			new BukkitRunnable() {

				int counter = 0;

				@Override
				public void run() {
					if (counter >= 20 * 3
							|| armorStand.getWorld().getBlockAt(armorStand.getEyeLocation()).getType().isSolid()) {
						armorStand.remove();
						cancel();
					} else {
						armorStand.teleport(armorStand.getLocation().add(vec.normalize().multiply(0.75)));
					}

					for (Entity entity : armorStand.getEyeLocation().getWorld()
							.getNearbyEntities(armorStand.getLocation(), 0.5, 0.75, 0.5)) {
						if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
							if (entity == player)
								continue;
							Bukkit.getPluginManager().callEvent(new CustomDamageEvent(entity, player, DamageReason.AUTO,
									MainClass.getPlugin().getCustomPlayerManager().getDamage(player), true));
							armorStand.remove();
							cancel();
						}
					}
					counter++;
				}
			}.runTaskTimer(MainClass.getPlugin(), 1, 1);
		}
	}

	@Override
	public void executeFirstAbility() {
		// Double shot

		shootBeam(player.getLocation().add(0, 0.5, 0), 13);
		Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

			@Override
			public void run() {
				shootBeam(player.getLocation().add(0, 0.5, 0), 13);
			}
		}, 2);
	}

	private void shootBeam(Location fromOrigin, double maxRange) {
		boolean enemyHit = false;

		Location toLocation = fromOrigin.clone()
				.add(player.getLocation().add(0, 0.5, 0).getDirection().normalize().multiply(maxRange));
		Location fromNew = fromOrigin.clone();
		Vector direction = toLocation.toVector().subtract(fromOrigin.toVector()).normalize();
		double range = Math.min(fromOrigin.distanceSquared(toLocation), maxRange * maxRange);
		while (fromOrigin.distanceSquared(fromNew) <= range && !enemyHit) {
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(Color.BLUE).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(Color.BLUE).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(Color.BLUE).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(Color.BLUE).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(Color.BLUE).display();

			for (Entity entity : fromNew.getChunk().getEntities()) {
				if (entity instanceof LivingEntity && !(entity instanceof ArmorStand) && entity != player)
					if (entity.getLocation().distanceSquared(fromNew) <= 2) {
						enemyHit = true;
						MainClass.getPlugin().getDamageManager().damageEntity(player, entity, DamageReason.PHYSICAL, 10,
								false);
						break;
					}
			}
			fromNew.add(direction);
		}

		// Do something to 'player' here, e.g. player.setHealth(player.getHealth() -
		// 2D);
	}

	@Override
	public void executeSecondAbility() {

	}

	@Override
	public void executeThirdAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeUltimate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopAllTasks() {

	}

}
