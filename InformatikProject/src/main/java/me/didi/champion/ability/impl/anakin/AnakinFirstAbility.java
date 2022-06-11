package me.didi.champion.ability.impl.anakin;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ColorTransition;
import me.didi.utilities.DefaultLightningCreator;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.Lightning;
import me.didi.utilities.MathUtils;
import me.didi.utilities.TaskManager;

public class AnakinFirstAbility implements Ability {

	private double length = 1.2;
	private double lengthMod = 1;
	private int maxChain = 25;
	private int maxLightnings = 250;
	private double maxJitter = 1;
	private double jitterMod = 1.03;
	private double splitChance = 0.1;
	private double splitMod = 1.02;
	private int rgbStart = new BigInteger("9922FF", 16).intValue();
	private int rgbEnd = new BigInteger("7777FF", 16).intValue();
	private double lightningDamage = 10;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.RED + "Force Lightning";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.DAYLIGHT_DETECTOR)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Anakin taps into the dark side of the force, unleashing",
				ChatColor.GRAY + "waves of force lightning, dealing",
				ChatColor.GREEN + "10 damage" + ChatColor.GRAY + " per wave" };
	}

	@Override
	public AbilityType getAbilityType() {
		// TODO Auto-generated method stub
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 20;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {

		abilityStateManager.addCooldown(player, 0, getCooldown());

		shootLightning(player);

	}

	private void shootLightning(Player player) {
		TaskManager.getInstance().repeatUntil(0, 1, 20 * 1, (backTask, counter) -> {

			Location dest = MathUtils.getLocationToLeft(
					player.getLocation().add(0, 0.5, 0).add(player.getLocation().getDirection().multiply(2)),
					MathUtils.getRandomBetween(-2, 2));

			Location playerLoc = MathUtils.getLocationToRight(player.getLocation().add(0, 0.6, 0), 0.3);

			Vector dir = dest.clone().subtract(playerLoc).toVector().normalize();

			TaskManager.getInstance().runTaskAsync(task -> {
				Collection<Lightning> lightnings = DefaultLightningCreator.createLightning(player.getWorld(), playerLoc,
						dir.clone(), length, lengthMod, maxChain, maxLightnings, maxJitter, jitterMod, splitChance,
						splitMod);
				TaskManager.getInstance().runTaskLater(0, syncTask -> {
					Set<Entity> entities = Lightning.create(lightnings, Lightning.NO_LIMIT,
							new ColorTransition(rgbStart, rgbEnd, Lightning.countTotalLightnings(lightnings)));
					if (lightningDamage < 0.0) {
						return;
					}

					entities.stream().filter(entity -> !entity.equals(player))
							.filter(entity -> entity instanceof LivingEntity)
							.filter(entity -> !(entity instanceof ArmorStand) && entity != player).forEach(entity -> {
								DamageManager.damageEntity(player, entity, DamageReason.MAGIC, lightningDamage, false);
							});
				});

			});
		});
	}

}
