package me.didi.champion.ability.impl.rex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class RexUltimate implements Ability {

	private Map<Player, BukkitTask> tasks = new HashMap<>();

	@Override
	public String getName() {
		return "Airstrike";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack icon = new ItemBuilder(new ItemStack(Material.BEACON)).setDisplayName(ChatColor.GOLD + getName())
				.setLore(getDescription()).toItemStack();
		return icon;
	}

	@Override
	public String[] getDescription() {

		return new String[] { ChatColor.GRAY + "Launches an airstrike dealing",
				ChatColor.RED + "20 damage" + ChatColor.GRAY + " per tick" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.OTHER;
	}

	@Override
	public int getCooldown() {
		return 20;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		Set<Material> transparent = new HashSet<>();
		transparent.add(Material.AIR);
		transparent.add(Material.WATER);
		transparent.add(Material.LAVA);
		transparent.add(Material.STATIONARY_LAVA);
		transparent.add(Material.STATIONARY_WATER);
		Location dest = player.getTargetBlock(transparent, 30).getLocation();
		dest.setY(player.getWorld().getHighestBlockYAt(dest));

		World world = player.getWorld();
		tasks.put(player, Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {
			int counter = 0;
			double radius = 0;

			@Override
			public void run() {
				if (counter >= 20 * 5) {
					tasks.get(player).cancel();
				}

				if (radius <= 5) {
					drawParticleCircle(radius, dest);
					radius += 0.25;
				} else {

					if (counter % 2 == 0) {
						Random random = new Random();
						drawCyl(radius * random.nextDouble(), dest);
						for (Entity entity : world.getNearbyEntities(dest, radius - 1, radius - 1, radius - 1)) {

							if (DamageManager.isEnemy(player, entity)) {
								DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, 20, false);
							}
						}
					}
				}
				counter++;
			}

			private void drawCyl(double radius, Location location) {
				for (double t = 0; t <= 2 * Math.PI * radius; t += 5) {
					double x = (radius * Math.cos(t)) + location.getX();
					double z = (location.getZ() + radius * Math.sin(t));
					Location lightning = new Location(world, x, location.getY(), z);
					world.strikeLightningEffect(lightning);
				}
			}

			private void drawParticleCircle(double radius, Location location) {
				for (double y = 1; y < world.getMaxHeight(); y *= 1.25) {
					for (double s = 0; s <= 2 * Math.PI * radius; s += 2 * Math.PI / 9) {
						double t = s + Math.toRadians(radius * 24);
						double x = (radius * Math.cos(t)) + location.getX();
						double z = (location.getZ() + radius * Math.sin(t));
						Location particle = new Location(world, x, location.getY(), z);
						ParticleEffect.REDSTONE.display(particle);

						particle = new Location(world, x, location.getY() + y, z);
						ParticleEffect.REDSTONE.display(particle);
					}
				}
				for (int y = 0; y <= 24; y += 6) {
					for (double t = 0; t <= 2 * Math.PI * radius; t += 2 * Math.PI / 50) {
						double x = (radius * Math.cos(t)) + location.getX();
						double z = (location.getZ() + radius * Math.sin(t));
						Location particle = new Location(world, x, location.getY() + y, z);
						ParticleEffect.REDSTONE.display(particle);
					}
				}

			}
		}, 1, 1));
		abilityStateManager.addCooldown(player, 3, getCooldown());
	}

}
