package me.didi.champion.ability.impl.brand;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.BurnEffect;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class BrandSecondAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.GOLD + "Pillar of Flame";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.BLAZE_ROD).setDisplayName(getName()).setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "After a delay, Brand erupts a pillar of flame at the target",
				ChatColor.GRAY + "location that deals " + ChatColor.DARK_AQUA + "magic damage " + ChatColor.GRAY
						+ "to enemies hit." };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		abilityStateManager.addCooldown(player, 1, getCooldown());
		Set<Material> materials = new HashSet<>();
		materials.add(Material.AIR);
		materials.add(Material.WATER);
		materials.add(Material.STATIONARY_WATER);
		materials.add(Material.LAVA);
		materials.add(Material.STATIONARY_LAVA);

		Location dest = player.getTargetBlock(materials, 9).getLocation();

		drawPillar(dest, player, specialEffectsManager);
	}

	private void drawPillar(Location location, Player player, SpecialEffectsManager specialEffectsManager) {

		TaskManager.getInstance().repeat(1, 1, new Consumer<BukkitTask>() {
			double radius = 5;
			double y = 0.1;

			@Override
			public void accept(BukkitTask task) {
				if (radius <= 1) {
					for (double y = 0; y <= 20; y += 0.5) {
						for (double t = 0; t < 2 * Math.PI; t += (2 * Math.PI / 40)) {
							double x = radius * Math.cos(t);
							double z = radius * Math.sin(t);

							Location out = location.clone().add(x, y, z);
							new ParticleBuilder(ParticleEffect.FLAME, out).setSpeed(0.2f).display();
						}
					}
					task.cancel();
					damageEnemies(location, player, specialEffectsManager);
					return;
				}

				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.ORANGE, location, 5);
				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.ORANGE, location.add(0, y, 0), radius);

				radius -= 0.25;

				y *= 1.05;
			}

			private void damageEnemies(Location location, Player player, SpecialEffectsManager specialEffectsManager) {
				player.getWorld().getLivingEntities().stream().filter(entity -> entity != player)
						.filter(entity -> !(entity instanceof ArmorStand)).collect(Collectors.toList())
						.forEach(entity -> {

							final double higherX = Math.max(entity.getLocation().getX(), location.getX());
							final double lowerX = Math.min(entity.getLocation().getX(), location.getX());

							final double higherZ = Math.max(entity.getLocation().getZ(), location.getZ());
							final double lowerZ = Math.min(entity.getLocation().getZ(), location.getZ());

							if (higherX - lowerX <= 5 && higherZ - lowerZ <= 5) {
								DamageManager.damageEntity(player, entity, DamageReason.MAGIC, 20, false);
								specialEffectsManager.addSpecialEffect(new BurnEffect(player, entity, 4, 3));
								entity.setFireTicks(entity.getFireTicks() + 4 * 19 - 1);
							}
						});
			}
		});
	}

}
