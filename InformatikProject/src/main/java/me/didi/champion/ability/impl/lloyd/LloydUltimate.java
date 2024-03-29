package me.didi.champion.ability.impl.lloyd;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.champion.ability.Recastable;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.CurrentStatGetter;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemSetter;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleEffect;

public class LloydUltimate extends Recastable implements Ability {

	static int perTurn = 40;
	static int turns = 4;
	static int particleCount = perTurn * (turns + 1);
	static float stepSize = (float) (2 * Math.PI / perTurn);
	static int red = 0;
	static int green = 255;
	static int blue = 25;

	static double[] x2Array = new double[27720];
	static double[] z2Array = new double[27720];
	static double[] y2Array = new double[27720];

	static double[] x3Array = new double[220];
	static double[] z3Array = new double[220];
	static double[] y3Array = new double[220];

	static float[] xArray = new float[particleCount];
	static float[] zArray = new float[particleCount];
	static {

		int index = 0;
		float increase = 2.00f / particleCount;
		float radius = 0f;
		for (double t = 0; t < 2 * Math.PI * (turns + 1); t += stepSize) {
			float x = radius * (float) Math.sin(t);
			float z = radius * (float) Math.cos(t);
			xArray[index] = x;
			zArray[index] = z;

			if (t < 2 * Math.PI * turns)
				radius += increase;
			index++;
		}

		int max_height = 2;
		double max_radius = 2;
		int lines = 7;
		double height_increasement = 0.2;
		double radius_increasement = max_radius / max_height;

		int counter = 0;
		for (int angle = 0; angle < 360; angle++) {
			for (int l = 0; l < lines; l++) {
				for (double y = 0; y < max_height; y += height_increasement) {
					double radius2 = y * radius_increasement;
					double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius2;
					double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius2;

					x2Array[counter] = x;
					z2Array[counter] = z;
					y2Array[counter] = y;
					counter++;
				}
			}
		}

		counter = 0;
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double radius3 = Math.sin(i);
			double y = Math.cos(i);
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				double x = Math.cos(a) * radius3;
				double z = Math.sin(a) * radius3;
				x3Array[counter] = x;
				z3Array[counter] = z;
				y3Array[counter] = y;
				counter++;
			}
		}
	}

	private Map<Player, BukkitTask> tasks = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public BiConsumer<Player, AbilityStateManager>[] getRecasts() {
		BiConsumer<Player, AbilityStateManager> recasts[] = new BiConsumer[3];
		recasts[0] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				airjitzu(player, abilityStateManager);
			}

		};

		recasts[1] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				cancelAirjitzu(player, abilityStateManager);
				spinjitzu(player, abilityStateManager);
			}

		};
		recasts[2] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				cancelSpinjitzu(player, abilityStateManager);
			}

		};

		return recasts;
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "Master of Spin";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack itemStack = new ItemBuilder(new ItemStack(Material.STRING)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
		return itemStack;
	}

	@Override
	public String[] getDescription() {
		String[] lore = new String[] { ChatColor.GRAY + "Lloyd casts airjitzu, temporarily gaining " + ChatColor.YELLOW
				+ "" + ChatColor.ITALIC + "flight.",
				ChatColor.GRAY + "He can recast this ability to start spinjitzu, which",
				ChatColor.GRAY + "deals " + ChatColor.RED + "physical damage (" + ChatColor.WHITE + "60"
						+ ChatColor.GOLD + " (+75% AD)" + ChatColor.DARK_PURPLE + "(+19% AP)" + ChatColor.RED + ") "
						+ ChatColor.GRAY + "over 10 seconds" };
		return lore;
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MOVEMENT;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public int getRecastCountdown() {
		return 10;
	}

	private void airjitzu(Player player, AbilityStateManager abilityStateManager) {

		new ItemSetter().setItem(player, 3, new ItemBuilder(getIcon().clone()).addGlow().toItemStack());
		player.setAllowFlight(true);
		player.setFlying(true);

		abilityStateManager.addRecastCooldown(player, 3, getRecastCountdown());

		tasks.put(player,
				TaskManager.getInstance().repeatUntil(1, 1, 20 * 10, new BiConsumer<BukkitTask, AtomicLong>() {
					int angle = 0;

					int max_height = 2;
					double max_radius = 2;
					int lines = 7;
					double height_increasement = 0.2;
					double radius_increasement = max_radius / max_height;
					Location loc = null;

					private void createSphere(Player player) {
						Location location = player.getLocation().add(0, 1, 0);
						for (int i = 0; i < x3Array.length; i++) {
							double x = x3Array[i];
							double z = z3Array[i];
							double y = y3Array[i];
							Location next = location.clone().add(x, y, z);
							ParticleEffect.REDSTONE.display(next, Color.GREEN);

						}
					}

					@Override
					public void accept(BukkitTask task, AtomicLong counter) {
						loc = player.getLocation().subtract(0, 1, 0);
						if (angle >= 360)
							angle = 0;

						for (int l = 0; l < lines; l++) {
							for (double y = 0; y < max_height; y += height_increasement) {
								double radius = y * radius_increasement;
								double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
								double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;

								Location next = loc.clone().add(x, y, z);
								ParticleEffect.REDSTONE.display(next, Color.GREEN);
							}
						}
						if (counter.get() % 3 == 0)
							createSphere(player);
						angle += 8;

						if (counter.get() >= 20 * 10) {
							abilityStateManager.addCooldown(player, 3, getCooldown());
							cancelAirjitzu(player, abilityStateManager);
							recastCounters.put(player, 0);
							return;
						}
					}
				}));

		recastCounters.put(player, 1);
	}

	private void cancelAirjitzu(Player player, AbilityStateManager abilityStateManager) {
		tasks.get(player).cancel();
		player.setFallDistance(0);
		player.setAllowFlight(false);
		player.setFlying(false);
	}

	private void spinjitzu(Player player, AbilityStateManager abilityStateManager) {
		abilityStateManager.removeRecastCooldown(player, this, 3);
		abilityStateManager.addRecastCooldown(player, 3, getRecastCountdown());

		tasks.put(player, TaskManager.getInstance().repeatUntil(1, 1, 20 * 10, (task, counter) -> {
			player.setFallDistance(0);
			if (counter.get() % 2 == 0) {
				spin(player);
				for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation().add(0, 1, 0), 1.75, 0.5,
						1.75)) {
					if (DamageManager.isEnemy(player, entity)) {
						double damage = CurrentStatGetter.getInstance().getAttackDamage(player) * 0.75
								+ CurrentStatGetter.getInstance().getAbilityPower(player) * 0.19 + 60;
						DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, damage / 10, true);
					}
				}
			}

			if (counter.get() >= 20 * 10) {
				cancelSpinjitzu(player, abilityStateManager);
			}
		}));

		new ItemSetter().setItem(player, 3, new ItemBuilder(getIcon().clone()).addGlow().toItemStack());
		recastCounters.put(player, recastCounters.get(player) + 1);
	}

	private void spin(Player player) {
		Location loc = player.getLocation().clone();
		float y = (float) loc.getY() - 0.25f;
		int index = 0;
		for (double t = 0; t < 2 * Math.PI * (turns + 1); t += stepSize) {
			float x = xArray[index];
			float z = zArray[index];

			Location newLoc = new Location(player.getWorld(), loc.getX() + x, y, loc.getZ() + z);
			ParticleEffect.REDSTONE.display(newLoc, Color.GREEN);
			if (t < 2 * Math.PI * turns)
				y += 2.5 / particleCount;
			index++;
		}
	}

	private void cancelSpinjitzu(Player player, AbilityStateManager abilityStateManager) {

		abilityStateManager.addCooldown(player, 3, getCooldown());
		tasks.get(player).cancel();

		recastCounters.put(player, 0);
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		int index = recastCounters.getOrDefault(player, 0);
		getRecasts()[index].accept(player, abilityStateManager);
	}

}
