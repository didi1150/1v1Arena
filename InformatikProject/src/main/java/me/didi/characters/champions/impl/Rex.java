package me.didi.characters.champions.impl;

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

		ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(),
				EntityType.ARMOR_STAND);
		armorStand.setArms(true);
		armorStand.setGravity(false);
		armorStand.setBasePlate(false);
		armorStand.setVisible(false);
		armorStand.setItemInHand(new ItemStack(Material.PRISMARINE_CRYSTALS));
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

				for (Entity entity : armorStand.getEyeLocation().getWorld().getNearbyEntities(armorStand.getLocation(),
						0.5, 0.75, 0.5)) {
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
