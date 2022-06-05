package me.didi.characters.champions.impl;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.MeleeChampion;
import me.didi.events.damageSystem.DamageReason;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemManager;
import xyz.xenondevs.particle.ParticleEffect;

public class Lloyd extends MeleeChampion {

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

	private int recastCooldown = 5;

	private int abilityCounter = 0;
	private BukkitTask bukkitTask;

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

	public Lloyd(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
		// TODO: ItemStack builder
//		Ability firstAbility = new OneTimeAbility("Shurikens",
//				new ItemBuilder(new ItemStack(Material.INK_SACK, (short) 15))
//						.setDisplayName(ChatColor.GOLD + "Shurikens")
//						.setLore(ChatColor.GRAY + "Throws out shurikens",
//								ChatColor.GRAY + "dealing " + ChatColor.RED + "60" + ChatColor.GRAY + " damage.")
//						.toItemStack());
//		Ability secondAbility = new OneTimeAbility("Disguise", null);
//
//		Ability thirdAbility = new OneTimeAbility("Blind", null);
//
//		RecastableAbility ultimateAbility = new RecastableAbility("Beyblade", null);
//		ultimateAbility.addFunction(new Callable<Player>() {
//
//			@Override
//			public Player call() throws Exception {
//				return getPlayer();
//			}
//		});
	}

	@Override
	public void executeAutoAttack() {
		// TODO
	}

	@Override
	public void executeFirstAbility() {

	}

	@Override
	public void executeSecondAbility() {

	}

	@Override
	public void executeThirdAbility() {

	}

	@Override
	public void executeUltimate() {
		switch (abilityCounter) {
		case 0:
			bukkitTask = airjitzu(player);
			break;
		case 1:
//			secondTask.cancel();
			bukkitTask.cancel();
			player.setFallDistance(0);
			player.setAllowFlight(false);
			player.setFlying(false);
			new ItemManager().setItem(player, 3, new ItemBuilder(getAbilities()[3].getIcon()).toItemStack());
			bukkitTask = Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

				@Override
				public void run() {
					abilityCooldownManager.addCooldown(player, 3, getAbilities()[3].getCooldown());
					abilityCounter = 0;
					bukkitTask.cancel();
				}
			}, 20 * (recastCooldown));
			abilityCooldownManager.addRecastCooldown(player, 3, recastCooldown);

			break;
		case 2:
			bukkitTask.cancel();
			abilityCooldownManager.removeRecastCooldown(player, getAbilities()[3]);
			bukkitTask = spinjitzu(player);
			break;
		case 3:
			bukkitTask.cancel();
			abilityCooldownManager.addCooldown(player, 3, getAbilities()[3].getCooldown());
			abilityCounter = -1;
			break;
		}
		abilityCounter++;
	}

	private BukkitTask spinjitzu(final Player player) {
		new ItemManager().setItem(player, 3,
				new ItemBuilder(getAbilities()[3].getIcon().clone()).addGlow().toItemStack());
		return Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {
			@Override
			public void run() {
				spin(player);
				for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation().add(0, 1, 0), 1.75, 0.5,
						1.75)) {
					if (isEnemy(entity))
						MainClass.getPlugin().getDamageManager().damageEntity(player, entity, DamageReason.PHYSICAL, 2,
								true);
				}
			}

		}, 0, 2);
	}

	private BukkitTask airjitzu(final Player player) {
		new ItemManager().setItem(player, 3,
				new ItemBuilder(getAbilities()[3].getIcon().clone()).addGlow().toItemStack());
		player.setAllowFlight(true);
		player.setFlying(true);
		return Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {

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
		}, 0, 1);
	}

	public void createSphere(Player player) {
		Location location = player.getLocation().add(0, 1, 0);
		for (int i = 0; i < x3Array.length; i++) {
			double x = x3Array[i];
			double z = z3Array[i];
			double y = y3Array[i];
			Location next = location.clone().add(x, y, z);
			ParticleEffect.REDSTONE.display(next, Color.GREEN);

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

	@Override
	public Champion clone() {
		return new Lloyd(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon(),
				getAutoAttackItem());
	}

	@Override
	public void stopAllTasks() {
		if (bukkitTask != null)
			bukkitTask.cancel();
	}
}
