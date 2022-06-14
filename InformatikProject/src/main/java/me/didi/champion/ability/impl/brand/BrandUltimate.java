package me.didi.champion.ability.impl.brand;

import java.awt.Color;
import java.util.function.Consumer;

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
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.BurnEffect;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.MathUtils;
import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class BrandUltimate implements Ability {

	@Override
	public String getName() {
		return ChatColor.GOLD + "Pyroclasm";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.GRAY + "Brand launches a fireball at the target enemy (" + ChatColor.DARK_AQUA + "50 damage"
						+ ChatColor.GRAY + "), which",
				ChatColor.GRAY + "fizzles after a few seconds, dealing " + ChatColor.DARK_AQUA + "25 damage" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 30;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		abilityStateManager.addCooldown(player, 3, getCooldown());
		MathUtils.shootProjectile(player, 7.5, new ItemStack(Material.FIREBALL), 50, false, 0.75,
				new ParticleBuilder(ParticleEffect.FLAME).setSpeed(0.1f), DamageReason.MAGIC, entity -> {
					drawUltimate(player, entity, 4, specialEffectsManager);

					entity.setFireTicks(entity.getFireTicks() + 4 * 20);
					specialEffectsManager.addSpecialEffect(new BurnEffect(player, entity, 4, 3));
				});
	}

	private void drawUltimate(Player player, Entity entity, double radius,
			SpecialEffectsManager specialEffectsManager) {
		TaskManager.getInstance().repeat(0, 1, new Consumer<BukkitTask>() {
			int angle = 0;

			@Override
			public void accept(BukkitTask task) {
				Location location = entity.getLocation();
				for (int i = 0; i < 360; i++) {
					double x = radius * Math.cos(Math.toRadians(i));
					double z = radius * Math.sin(Math.toRadians(i));

					if (i < angle)
						ParticleEffect.REDSTONE.display(location.clone().add(x, 0, z), Color.GRAY);
					else
						ParticleEffect.FLAME.display(location.clone().add(x, 0, z));
				}

				if (angle >= 360) {
					ParticleUtils.drawCircle(new ParticleBuilder(ParticleEffect.FLAME).setSpeed(1), location, radius);
					DamageManager.damageEntity(player, entity, DamageReason.MAGIC, 25, false);
					entity.setFireTicks(4 * 20);
					specialEffectsManager.addSpecialEffect(new BurnEffect(player, entity, 4, 3));
					entity.setFireTicks(entity.getFireTicks() + 4 * 20);

					task.cancel();
				}
				angle += 10;
			}

		});
	}

}
