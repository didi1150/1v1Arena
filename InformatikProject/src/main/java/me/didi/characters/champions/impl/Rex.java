package me.didi.characters.champions.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
import me.didi.utilities.ArmorStandFactory;

public class Rex extends RangedChampion {

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public Champion clone() {
		return new Rex(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon(),
				getAutoAttackItem());
	}

	@Override
	public void executeAutoAttack() {

		ArmorStand armorStand = ArmorStandFactory
				.buildArmorStand(player.getLocation().add(player.getLocation().getDirection().normalize()), true, true);
		armorStand.setHelmet(new ItemStack(Material.PRISMARINE_CRYSTALS));

		new BukkitRunnable() {
			Location destination = player.getLocation().clone()
					.add(player.getLocation().getDirection().normalize().multiply(10));
			Location currentLocation = armorStand.getLocation();

			@Override
			public void run() {

				if (armorStand.getLocation().distanceSquared(destination) <= 1) {
					armorStand.remove();
					cancel();
				} else {
					Vector vec = destination.subtract(currentLocation).toVector().multiply(0.01);
					armorStand.teleport(currentLocation.add(vec));
				}

				for (Entity entity : armorStand.getEyeLocation().getWorld().getNearbyEntities(currentLocation, 0.5, 0.5,
						0.5)) {
					if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
						Bukkit.getPluginManager().callEvent(new CustomDamageEvent(entity, player, DamageReason.AUTO,
								MainClass.getPlugin().getCustomPlayerManager().getDamage(player), true));
						armorStand.remove();
						cancel();
					}
				}

				currentLocation = armorStand.getLocation();
			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);

	}

	@Override
	public void executeFirstAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeSecondAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeThirdAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeUltimate() {
		// TODO Auto-generated method stub

	}

}
