package me.didi.champion.characters.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.characters.RangedChampion;
import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageReason;
import me.didi.utilities.MathUtils;

public class Rex extends RangedChampion {

	private List<Player> cooldowns = new ArrayList<>();

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {
		if (cooldowns.contains(player))
			return;

		cooldowns.add(player);
		Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

			@Override
			public void run() {
				cooldowns.remove(player);
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
				Block blockAt = armorStand.getWorld()
						.getBlockAt(MathUtils.getLocationToRight(armorStand.getLocation().add(0, 0.5, 0), 0.3));
				if (counter >= 20 * 3 || blockAt.getType().isSolid()
						|| armorStand.getLocation().distanceSquared(destination) <= 2) {
					armorStand.remove();
					cancel();
				} else {
					armorStand.teleport(armorStand.getLocation().add(vec.normalize().multiply(0.75)));
				}

				for (Entity entity : armorStand.getEyeLocation().getWorld().getNearbyEntities(armorStand.getLocation(),
						0.5, 0.75, 0.5)) {
					if (!(entity instanceof LivingEntity))
						continue;
					if (entity instanceof ArmorStand)
						continue;
					if (entity == player)
						continue;
					Bukkit.getPluginManager().callEvent(new CustomDamageEvent(entity, player, DamageReason.AUTO,
							customPlayerManager.getDamage(player), true));
					armorStand.remove();
					cancel();
				}
				counter++;
			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);
	}

	@Override
	public void executeFirstAbility() {
		getAbilities()[0].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeSecondAbility() {
		getAbilities()[1].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeThirdAbility() {

		getAbilities()[2].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeUltimate() {

		getAbilities()[3].execute(abilityStateManager, player, specialEffectsManager);
	}
}
