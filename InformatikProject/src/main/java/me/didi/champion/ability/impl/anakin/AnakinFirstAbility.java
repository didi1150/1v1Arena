package me.didi.champion.ability.impl.anakin;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ColorTransition;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleEffect;

public class AnakinFirstAbility implements Ability {

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
		return new String[] { ChatColor.GRAY + "Anakin steps into the dark side of the force, sending",
				ChatColor.GRAY + "out waves of force lightning, dealing",
				ChatColor.GREEN + "10 damage" + ChatColor.GRAY + " each" };
	}

	@Override
	public AbilityType getAbilityType() {
		// TODO Auto-generated method stub
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {

		abilityStateManager.addCooldown(player, 0, getCooldown());

		shootLightning(player);

	}

	private void shootLightning(Player player) {
		Random r = new Random();
		double maxJitter = 2.0;
		Vector lightningEnd = player.getLocation().clone().toVector()
				.add(player.getLocation().getDirection().clone().normalize().multiply(13))
				.add(new Vector((r.nextDouble() - 0.5), (r.nextDouble() - 0.5), (r.nextDouble() - 0.5)).normalize()
						.multiply((r.nextDouble() - 0.5) * maxJitter));
		Location locationEnd = lightningEnd.toLocation(player.getWorld());

		TaskManager.getInstance().repeatUntil(0, 10, 20 * 10, (task, counter) -> {
			create(player, player.getLocation(), locationEnd, 000066, new java.awt.Color(204, 230, 255).getRGB(),
					getCooldown());
		});

	}

	public Set<Entity> create(Player player, Location start, Location end, int rgbStart, int rgbEnd,
			float distBetween) {
		int amountParticles = getParticleAmount(start, end, distBetween);
		ColorTransition transition = new ColorTransition(rgbStart, rgbEnd, amountParticles);
		Vector dir = end.clone().toVector().subtract(start.clone().toVector()).normalize().multiply(distBetween);
		Set<Entity> entities = Sets.newHashSet();
		for (int index = 0; index < amountParticles; index++) {
			Color color = transition.nextColor();
			Location particleLoc = start.clone().add(dir.clone().multiply(index));

			ParticleEffect.REDSTONE.display(particleLoc,
					new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue()));

			entities.addAll(particleLoc.getWorld().getNearbyEntities(particleLoc, 1, 1, 1).stream()
					.filter(entity -> DamageManager.isEnemy(player, entity)).collect(Collectors.toSet()));
		}
		return entities;
	}

	private int getParticleAmount(Location start, Location end, float distBetween) {
		int amountParticles = (int) (start.distance(end) / distBetween);
		if (amountParticles == 0) {
			return 0;
		}
		return amountParticles;

	}

}
