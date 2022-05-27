package me.didi.characters.champions.impl;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
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

public class Lloyd extends MeleeChampion {

	static int perTurn = 40;
	static int turns = 4;
	static int particleCount = perTurn * (turns + 1);
	static float stepSize = (float) (2 * Math.PI / perTurn);
	static int red = 0;
	static int green = 255;
	static int blue = 25;

	private int cooldown = 10;
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
	}

	public Lloyd(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon);
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
			bukkitTask.cancel();
			new ItemManager().setItem(player, 3, new ItemBuilder(getAbilities()[3].getIcon()).toItemStack());
			bukkitTask = Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

				@Override
				public void run() {
					abilityCooldownManager.addCooldown(player, 3, cooldown);
					abilityCounter = 0;
					bukkitTask.cancel();
				}
			}, 20 * (recastCooldown + 1));
			abilityCooldownManager.addRecastCooldown(player, 3, recastCooldown);

			break;
		case 2:
			bukkitTask.cancel();
			abilityCooldownManager.removeRecastCooldown(player, getAbilities()[3]);
			bukkitTask = spinjitzu(player);
			break;
		case 3:
			bukkitTask.cancel();
			abilityCooldownManager.addCooldown(player, 3, cooldown);
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
				for (Entity entity : player.getWorld().getNearbyEntities(player.getEyeLocation(), 1, 0.25, 1)) {
					if (entity == player)
						continue;
					if (entity instanceof LivingEntity && !(entity instanceof ArmorStand))
						MainClass.getPlugin().getDamageManager().damageEntity(player, entity, DamageReason.PHYSICAL, 2,
								true);
				}
			}

		}, 0, 2);
	}

	private BukkitTask airjitzu(final Player player) {
		new ItemManager().setItem(player, 3,
				new ItemBuilder(getAbilities()[3].getIcon().clone()).addGlow().toItemStack());
		return Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {
			@Override
			public void run() {
				spin(player);
			}

		}, 0, 2);
	}

	private void spin(Player player) {

		Location loc = player.getLocation();
		float y = (float) player.getLocation().getY() - 0.25f;
		int index = 0;
		for (double t = 0; t < 2 * Math.PI * (turns + 1); t += stepSize) {
			float x = xArray[index];
			float z = zArray[index];
			// amount, red, green, blue, speed

			player.getLocation().getWorld().spigot().playEffect(
					new Location(player.getWorld(), loc.getX() + x, y, loc.getZ() + z), Effect.COLOURED_DUST, 0, 1, red,
					green, blue, 10, 0, 64);
//			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true,
//					((float) loc.getX()) + x, y, (float) (loc.getZ() + z), red, green, blue, (float) 1, 0);
//			for (Player pl : Bukkit.getOnlinePlayers()) {
//				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
//			}
			if (t < 2 * Math.PI * turns)
				y += 2.5 / particleCount;
			index++;
		}
	}

	@Override
	public Champion clone() {
		return new Lloyd(getName(), getAbilities(), abilityCounter, abilityCounter, abilityCounter, getIcon());
	}
}
