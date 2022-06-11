package me.didi.champion.ability.impl.anakin;

import java.awt.Color;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import me.didi.utilities.MathUtils;
import xyz.xenondevs.particle.ParticleEffect;

public class AnakinUltimate implements Ability {

	private double t = Math.PI / 4;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.WHITE + "Unleashed Force";
	}

	@Override
	public ItemStack getIcon() {
		// TODO Auto-generated method stub
		return new ItemBuilder(new ItemStack(Material.RABBIT_FOOT)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Anakin uses the force on the next player he sees. If",
				ChatColor.GRAY + " he detects a player, he throws the enemy up",
				ChatColor.GRAY + " and slams him back into the ground, dealing " + ChatColor.RED + "90 damage"
						+ ChatColor.GRAY + ".",
				ChatColor.GRAY + "Afterwards, the target is slowed down for 5 seconds." };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.RANGED;
	}

	@Override
	public int getCooldown() {
		return 20;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		abilityStateManager.addCooldown(player, 3, getCooldown());

		Player target = MathUtils.getTargetPlayer(player, 20);
		if (target == null)
			return;

		Location top = target.getLocation().add(0, 30, 0);

		Location bot = MathUtils.getHighestLocation(target.getLocation());
		TaskManager.getInstance().repeatUntil(0, 1, 20 * 5, (task, counter) -> {

			Vector topVec = top.clone().subtract(bot.clone()).toVector();

			Vector botVec = bot.clone().subtract(top.clone()).toVector();

			if (counter.get() >= 20 * 2) {
				target.setVelocity(botVec.clone());
				target.setAllowFlight(true);
				if (target.isOnGround()) {
					target.setAllowFlight(false);
					DamageManager.damageEntity(player, target, DamageReason.PHYSICAL, 90, false);

					target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 3, false, false));
					task.cancel();
					displayParticles(target);
					return;
				}
			} else {
				target.setVelocity(topVec.clone().normalize());
			}
		});

	}

	private void displayParticles(Player player) {
		TaskManager.getInstance().repeat(0, 1, task -> {

			Location loc = player.getLocation();
			t = t + 0.2 * Math.PI;
			for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32) {
				if (t > 3) {
					double x = t * Math.cos(theta);
					double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
					double z = t * Math.sin(theta);
					loc.add(x, y, z);

					ParticleEffect.REDSTONE.display(loc, Color.YELLOW);
					loc.subtract(x, y, z);

					theta = theta + Math.PI / 64;

					x = t * Math.cos(theta);
					y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
					z = t * Math.sin(theta);
					loc.add(x, y, z);
					ParticleEffect.REDSTONE.display(loc, Color.GRAY);
					loc.subtract(x, y, z);
				}
			}
			if (t > 20) {
				t = Math.PI / 4;
				task.cancel();
				return;
			}
		});
	}

}
