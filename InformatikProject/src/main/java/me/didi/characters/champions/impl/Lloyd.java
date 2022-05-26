package me.didi.characters.champions.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Lloyd extends MeleeChampion {

	private int abilityCounter = 0;
	private BukkitTask bukkitTask;

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
			abilityCooldownManager.addRecastCooldown(player, 3, 5);
			bukkitTask = Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

				@Override
				public void run() {
					abilityCooldownManager.addCooldown(player, 3, 10);
					abilityCounter = 0;
					bukkitTask.cancel();
				}
			}, 20 * 6);

			break;
		case 2:
			bukkitTask.cancel();
			abilityCooldownManager.removeRecastCooldown(player, getAbilities()[3]);
			bukkitTask = spinjitzu(player);
			break;
		case 3:
			bukkitTask.cancel();
			abilityCooldownManager.addCooldown(player, 3, 10);
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
				for (Entity entity : player.getWorld().getNearbyEntities(player.getEyeLocation(), 1, 0.5, 1)) {
					if (entity == player)
						continue;
					if (entity instanceof LivingEntity && !(entity instanceof ArmorStand))
						MainClass.getPlugin().getDamageManager().damageEntity(player, entity, DamageReason.PHYSICAL, 2,
								true);
				}
			}

		}, 0, 0);
	}

	private BukkitTask airjitzu(final Player player) {
		new ItemManager().setItem(player, 3,
				new ItemBuilder(getAbilities()[3].getIcon().clone()).addGlow().toItemStack());
		return Bukkit.getScheduler().runTaskTimer(MainClass.getPlugin(), new Runnable() {
			@Override
			public void run() {
				spin(player);
			}

		}, 0, 0);
	}

	private void spin(Player player) {
		float increase = 0.002f;
		float radius = 0f;

		Location loc = player.getLocation();
		float y = (float) player.getLocation().getY() - 0.5f;

		for (double t = 0; t <= 50; t += 0.05f) {
			float x = radius * (float) Math.sin(t);
			float z = radius * (float) Math.cos(t);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true,
					((float) loc.getX()) + x, y, (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
			for (Player pl : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
			}
			y += 0.003f;
			radius += increase;
		}

		for (double t = 0; t <= 50; t += 0.5f) {
			float x = radius * (float) Math.sin(t);
			float z = radius * (float) Math.cos(t);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true,
					((float) loc.getX()) + x, y, (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
			for (Player pl : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	@Override
	public Champion clone() {
		return new Lloyd(getName(), getAbilities(), abilityCounter, abilityCounter, abilityCounter, getIcon());
	}
}
