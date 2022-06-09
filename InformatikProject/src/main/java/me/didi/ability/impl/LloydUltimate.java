package me.didi.ability.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.ability.AbilityStateManager;
import me.didi.ability.AbilityType;
import me.didi.ability.Recastable;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemManager;
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
		BiConsumer<Player, AbilityStateManager> recasts[] = new BiConsumer[4];
		recasts[0] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				airjitzu(player);
			}

		};

		recasts[1] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				cancelAirjitzu(player, abilityStateManager);
			}

		};

		recasts[2] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				spinjitzu(player, abilityStateManager);
			}

		};

		recasts[3] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				cancelSpinjitzu(player, abilityStateManager);
			}

		};
		return recasts;
	}

	@Override
	public String getName() {
		return "Master of Spin";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack itemStack = new ItemBuilder(new ItemStack(Material.STRING)).setDisplayName(ChatColor.GOLD + getName())
				.setLore(getDescription()).toItemStack();
		return itemStack;
	}

	@Override
	public String[] getDescription() {
		String[] lore = new String[] { ChatColor.GRAY + "Lloyd casts airjitzu, which he",
				ChatColor.GRAY + "can recast into Spinjitzu" };
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
		return 5;
	}

	private void airjitzu(Player player) {

		new ItemManager().setItem(player, 3, new ItemBuilder(getIcon().clone()).addGlow().toItemStack());
		player.setAllowFlight(true);
		player.setFlying(true);
		tasks.put(player, Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {

			int angle = 0;

			int max_height = 2;
			double max_radius = 2;
			int lines = 7;
			double height_increasement = 0.2;
			double radius_increasement = max_radius / max_height;
			Location loc = null;

			@Override
			public void run() {
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

				createSphere(player);
				angle += 8;
			}

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
		}, 0, 1));

		recastCounters.put(player, 1);
	}

	private void cancelAirjitzu(Player player, AbilityStateManager abilityStateManager) {

		tasks.get(player).cancel();
		player.setFallDistance(0);
		player.setAllowFlight(false);
		player.setFlying(false);

		tasks.put(player, Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

			@Override
			public void run() {
				abilityStateManager.addCooldown(player, 3, getCooldown());
				recastCounters.put(player, 0);
				tasks.get(player).cancel();
			}
		}, 20 * getRecastCountdown()));
		abilityStateManager.addRecastCooldown(player, 3, getRecastCountdown());

		recastCounters.put(player, recastCounters.get(player) + 1);
	}

	private void spinjitzu(Player player, AbilityStateManager abilityStateManager) {
		tasks.get(player).cancel();
		abilityStateManager.removeRecastCooldown(player, this, 3);
		new ItemManager().setItem(player, 3, new ItemBuilder(getIcon().clone()).addGlow().toItemStack());

		tasks.put(player, Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {
			@Override
			public void run() {
				spin(player);
				for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation().add(0, 1, 0), 1.75, 0.5,
						1.75)) {
					if (DamageManager.isEnemy(player, entity))
						DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, 2, true);
				}
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

		}, 0, 2));

		recastCounters.put(player, recastCounters.get(player) + 1);
	}

	private void cancelSpinjitzu(Player player, AbilityStateManager abilityStateManager) {

		tasks.get(player).cancel();
		abilityStateManager.addCooldown(player, 3, getCooldown());

		recastCounters.put(player, 0);
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player) {
		int index = recastCounters.getOrDefault(player, 0);
		getRecasts()[index].accept(player, abilityStateManager);
	}

}
