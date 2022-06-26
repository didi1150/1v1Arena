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

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.CurrentStatGetter;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleEffect;

public class RexUltimate implements Ability {

	private Map<Player, BukkitTask> tasks = new HashMap<>();
	private Map<Player, Double> raidii = new HashMap<Player, Double>();

	@Override
	public String getName() {
		return ChatColor.GOLD + "Airstrike";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack icon = new ItemBuilder(new ItemStack(Material.BEACON)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
		return icon;
	}

	@Override
	public String[] getDescription() {

		return new String[] { ChatColor.GRAY + "Launches an airstrike dealing",
				ChatColor.RED + "physical damage (" + ChatColor.WHITE + "150" + ChatColor.GOLD + " (+80% AD)"
						+ ChatColor.RED + ")" + ChatColor.GRAY + " over 5 seconds" };
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
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		abilityStateManager.addCooldown(player, 3, getCooldown());
		Set<Material> transparent = new HashSet<>();
		transparent.add(Material.AIR);
		transparent.add(Material.WATER);
		transparent.add(Material.LAVA);
		transparent.add(Material.STATIONARY_LAVA);
		transparent.add(Material.STATIONARY_WATER);
		Location dest = player.getTargetBlock(transparent, 30).getLocation();
		dest.setY(player.getWorld().getHighestBlockYAt(dest));
		raidii.put(player, 0.00);
		World world = player.getWorld();
		tasks.put(player, TaskManager.getInstance().repeatUntil(1, 1, 20 * 5, (task, counter) -> {

			if (raidii.get(player) <= 5) {
				drawParticleCircle(raidii.get(player), dest, dest.getWorld());
				raidii.put(player, raidii.get(player) + 0.25);
			} else {

				Random random = new Random();
				drawCyl(raidii.get(player) * random.nextDouble(), dest, dest.getWorld());
				for (Entity entity : world.getLivingEntities()) {
					double higherX = Math.max(entity.getLocation().getX(), dest.getX());
					double lowerX = Math.min(entity.getLocation().getX(), dest.getX());

					double higherZ = Math.max(entity.getLocation().getZ(), dest.getZ());
					double lowerZ = Math.min(entity.getLocation().getZ(), dest.getZ());

					if (higherX - lowerX <= raidii.get(player) && higherZ - lowerZ <= raidii.get(player)) {

						if (DamageManager.isEnemy(player, entity)) {
							double damage = 150 + CurrentStatGetter.getInstance().getAttackDamage(player) * 0.8;
							DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, damage / 100, false);
						}
					}
				}
			}
		}));
	}

	private void drawCyl(double radius, Location location, World world) {
		for (double t = 0; t <= 2 * Math.PI * radius; t += 5) {
			double x = (radius * Math.cos(t)) + location.getX();
			double z = (location.getZ() + radius * Math.sin(t));
			Location lightning = new Location(world, x, location.getY(), z);
			world.strikeLightningEffect(lightning);
		}
	}

	private void drawParticleCircle(double radius, Location location, World world) {
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

}
