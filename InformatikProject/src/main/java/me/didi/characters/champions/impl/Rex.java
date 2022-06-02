package me.didi.characters.champions.impl;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.RangedChampion;
import me.didi.events.damageSystem.CustomDamageEvent;
import me.didi.events.damageSystem.DamageReason;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;
import me.didi.utilities.VectorUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class Rex extends RangedChampion {

	private boolean isOnCooldown;
	private BukkitTask bukkitTask;

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

		shootBeam(player.getLocation().add(0, 0.5, 0), 13, false);
		Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

			@Override
			public void run() {
				shootBeam(player.getLocation().add(0, 0.5, 0), 13, true);
			}
		}, 3);
		abilityCooldownManager.addCooldown(player, 0, getAbilities()[0].getCooldown());
	}

	private void shootBeam(Location fromOrigin, double maxRange, boolean left) {
		boolean enemyHit = false;

		Location toLocation = fromOrigin.clone().add(fromOrigin.clone().getDirection().normalize().multiply(maxRange))
				.add(0, 1, 0);
		Location fromNew = VectorUtils.getLocationToRight(fromOrigin.clone(), 0.3);
		if (left)
			fromNew = VectorUtils.getLocationToLeft(fromOrigin.clone(), 0.3);

		Vector direction = toLocation.toVector().subtract(fromNew.toVector()).normalize();
		double range = Math.min(fromOrigin.distanceSquared(toLocation), maxRange * maxRange);
		while (fromOrigin.distanceSquared(fromNew) <= range && !enemyHit) {
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(new Color(0, 128, 255)).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(new Color(0, 128, 255)).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(new Color(0, 128, 255)).display();

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
		throwBomb();
		abilityCooldownManager.addCooldown(player, 1, getAbilities()[1].getCooldown());
	}

	@Override
	public void executeThirdAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeUltimate() {
		Set<Material> transparent = new HashSet<>();
		transparent.add(Material.AIR);
		Location dest = player.getTargetBlock(transparent, 30).getLocation();
		dest.setY(player.getWorld().getHighestBlockYAt(dest));

		World world = player.getWorld();
		bukkitTask = Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {
			int counter = 0;
			double radius = 0;

			@Override
			public void run() {
				if (counter >= 20 * 5) {
					bukkitTask.cancel();
				}

				if (radius <= 5) {
					drawParticleCircle(radius, dest);
					radius += 0.25;
				} else {

					if (counter % 2 == 0) {
						drawParticleCircle(radius, dest);
						drawCyl(radius, dest);
						for (Entity entity : world.getNearbyEntities(dest, radius, radius, radius)) {
							if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
								if (entity != player) {
									MainClass.getPlugin().getDamageManager().damageEntity(player, entity,
											DamageReason.PHYSICAL, 20, false);
								}
							}
						}
					}

				}
				counter++;
			}

			private void drawCyl(double radius, Location location) {
				for (double t = 0; t <= 2 * Math.PI * radius; t += 1) {
					double x = (radius * Math.cos(t)) + location.getX();
					double z = (location.getZ() + radius * Math.sin(t));
					Location particle = new Location(world, x, location.getY() + 1, z);
					world.strikeLightningEffect(particle);
				}
			}

			private void drawParticleCircle(double radius, Location location) {
				for (double t = 0; t <= 2 * Math.PI * radius; t += 0.05) {
					double x = (radius * Math.cos(t)) + location.getX();
					double z = (location.getZ() + radius * Math.sin(t));
					Location particle = new Location(world, x, location.getY() + 1, z);
					ParticleEffect.REDSTONE.display(particle);
				}
			}
		}, 1, 1);
		abilityCooldownManager.addCooldown(player, 3, getAbilities()[3].getCooldown());
	}

	@Override
	public void stopAllTasks() {
		bukkitTask.cancel();
	}

	public Vector calculateVelocity(Vector from, Vector to, int heightGain) {
		// Gravity of a potion
		double gravity = 0.115;
		// Block locations
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));
		// Height gain
		int gain = heightGain;
		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
		// Solve quadratic equation for velocity
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;
		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
		// Vertical velocity
		double vy = Math.sqrt(maxGain * gravity);
		// Horizontal velocity
		double vh = vy / slope;
		// Calculate horizontal direction
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;
		// Horizontal velocity components
		double vx = vh * dirx;
		double vz = vh * dirz;
		return new Vector(vx, vy, vz);
	}

	private double distanceSquared(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();
		return dx * dx + dz * dz;
	}

	public Entity spawnInvisibleArmorStand(Location l) {
		// You can remove the net.minecraft.server.v1_8_R3 and just import the classes
		// You need to change v1_8_R3 for your version.
		net.minecraft.server.v1_8_R3.World w = ((CraftWorld) l.getWorld()).getHandle();
		net.minecraft.server.v1_8_R3.EntityArmorStand nmsEntity = new net.minecraft.server.v1_8_R3.EntityArmorStand(w);
		// Yes, yaw goes first here ->
		nmsEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		nmsEntity.setInvisible(true);
		nmsEntity.setArms(false);
		nmsEntity.setBasePlate(false);
		/*
		 * You can make other changes like: nmsEntity.setGravity(false);
		 * nmsEntity.setArms(true); nmsEntity.setBasePlate(false); The methods are very
		 * similiar to the ArmorStand ones in the API
		 */
		w.addEntity(nmsEntity);
		return nmsEntity.getBukkitEntity();
	}

	private void throwBomb() {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation(entityPlayer, 0);
		entityPlayer.playerConnection.sendPacket(packet);
		ArmorStand as = (ArmorStand) spawnInvisibleArmorStand(
				VectorUtils.getLocationToRight(player.getLocation().add(0, 1, 0), 0.3));
		as.setItemInHand(
				ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_BOMB.getValue(), SkullFactory.HEAD_BOMB.getName()));
		Location dest = player.getLocation().clone().add(player.getLocation().getDirection().multiply(25));
		as.setVelocity(calculateVelocity(as.getLocation().toVector(), dest.toVector(), 2));
		new BukkitRunnable() {

			@Override
			public void run() {
				if (as.getWorld().getBlockAt(as.getLocation().add(0, -0.5, 0)).getType() != Material.AIR) {

					as.remove();

					ParticleEffect.EXPLOSION_NORMAL.display(as.getLocation());
					ParticleEffect.EXPLOSION_HUGE.display(as.getLocation());

					this.cancel();
				}
			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);
	}
}
