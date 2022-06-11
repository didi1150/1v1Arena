package me.didi.champion.ability.impl.rex;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.MathUtils;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class RexFirstAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.AQUA + "Blast'em";
	}

	@Override
	public ItemStack getIcon() {

		return new ItemBuilder(new ItemStack(Material.ARROW)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Rex quickly fires with his",
				ChatColor.GRAY + "two blasters, dealing " + ChatColor.RED + "10 damage " + ChatColor.GRAY + "each" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.RANGED;
	}

	@Override
	public int getCooldown() {
		return 5;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {

		abilityStateManager.addCooldown(player, 0, getCooldown());
		shootBeam(player.getLocation().add(0, 0.5, 0), 13, false, player);
		Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

			@Override
			public void run() {
				shootBeam(player.getLocation().add(0, 0.5, 0), 13, true, player);
			}
		}, 3);

	}

	private void shootBeam(Location fromOrigin, double maxRange, boolean left, Player player) {
		boolean enemyHit = false;

		Location toLocation = fromOrigin.clone().add(fromOrigin.clone().getDirection().normalize().multiply(maxRange))
				.add(0, 1, 0);
		Location fromNew = MathUtils.getLocationToRight(fromOrigin.clone(), 0.3);
		if (left)
			fromNew = MathUtils.getLocationToLeft(fromOrigin.clone(), 0.3);

		Vector direction = toLocation.toVector().subtract(fromNew.toVector()).normalize();
		double range = Math.min(fromOrigin.distanceSquared(toLocation), maxRange * maxRange);
		while (fromOrigin.distanceSquared(fromNew) <= range && !enemyHit) {
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(new Color(0, 128, 255)).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(new Color(0, 128, 255)).display();
			new ParticleBuilder(ParticleEffect.REDSTONE, fromNew).setColor(new Color(0, 128, 255)).display();

			for (Entity entity : player.getWorld().getNearbyEntities(fromNew, 0.5, 0.5, 0.5)) {
				if (DamageManager.isEnemy(player, entity)) {
					enemyHit = true;
					DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, 10, false);
					break;
				}
			}
			fromNew.add(direction);
		}

		// Do something to 'player' here, e.g. player.setHealth(player.getHealth() -
		// 2D);
	}
}
