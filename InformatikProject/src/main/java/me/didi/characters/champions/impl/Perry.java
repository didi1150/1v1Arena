package me.didi.characters.champions.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.MeleeChampion;
import me.didi.events.damageSystem.DamageReason;
import me.didi.utilities.ArmorStandFactory;

public class Perry extends MeleeChampion {

	public Perry(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public Champion clone() {
		return new Perry(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon(),
				getAutoAttackItem());
	}

	@Override
	public void executeAutoAttack() {

	}

	private void shootBoomerang() {
		Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(10)).add(0, 0.5,
				0);

		ArmorStand armorStand = (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(player.getLocation());
		armorStand.setMarker(true);
		armorStand.setGravity(false);
		armorStand.setArms(true);
		armorStand.setItemInHand(new ItemStack(Material.LEATHER_HELMET));
		armorStand.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(120), Math.toRadians(0)));

		proj.add(armorStand);

		new BukkitRunnable() {
			int distance = 10;
			int counter = 0;
			Vector vector = destination.subtract(player.getLocation()).toVector().multiply(0.25).normalize();

			@Override
			public void run() {

				EulerAngle rotation = armorStand.getRightArmPose();
				EulerAngle addRotation = rotation.add(0, 20, 0);

				armorStand.setRightArmPose(addRotation);

				if (counter >= distance) {
					vector = player.getLocation().add(0, 0.5, 0).toVector()
							.subtract(armorStand.getLocation().toVector()).normalize();
					armorStand.teleport(armorStand.getLocation().add(vector));

					if (counter >= distance * 2) {
						armorStand.remove();
						cancel();
					}
				} else {
					armorStand.teleport(armorStand.getLocation().add(vector));
				}

				if (counter % 5 == 0) {
					for (Entity entity : armorStand.getWorld().getNearbyEntities(armorStand.getLocation().add(0, 1, 0),
							1, 1, 1)) {
						if (isEnemy(entity)) {
							MainClass.getPlugin().getDamageManager().damageEntity(player, entity, DamageReason.PHYSICAL,
									20, false);
						}
					}
				}

				counter++;

			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);
	}

	@Override
	public void executeFirstAbility() {
		shootBoomerang();
	}

	@Override
	public void executeSecondAbility() {

	}

	@Override
	public void executeThirdAbility() {

	}

	@Override
	public void executeUltimate() {

	}

	@Override
	public void stopAllTasks() {

	}

}
