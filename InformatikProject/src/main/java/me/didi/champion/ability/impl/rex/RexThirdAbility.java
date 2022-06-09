package me.didi.champion.ability.impl.rex;

import java.awt.Color;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.RootEffect;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.VectorUtils;
import xyz.xenondevs.particle.ParticleEffect;

public class RexThirdAbility implements Ability {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.AQUA + "Immoblizer";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.DIAMOND_BARDING)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Rex fires an immobilizing shot,",
				ChatColor.GRAY + "causing the first enemy hit to be " + ChatColor.WHITE + "rooted",
				ChatColor.GRAY + " for 1.5 seconds" };
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
		abilityStateManager.addCooldown(player, 2, getCooldown());
		new BukkitRunnable() {
			Location dest = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(13))
					.add(0, 1, 0);
			Location newLoc = VectorUtils.getLocationToRight(player.getLocation().add(0, 0.6, 0), 0.3);
			Vector toVec = dest.toVector().subtract(newLoc.toVector()).normalize().multiply(0.5);

			@Override
			public void run() {

				if (newLoc.distanceSquared(dest) <= 2) {
					cancel();
					return;
				} else {
					ParticleEffect.REDSTONE.display(newLoc, Color.CYAN);
					ParticleEffect.REDSTONE.display(newLoc, Color.CYAN);
					ParticleEffect.REDSTONE.display(newLoc, Color.CYAN);

					player.getWorld().getNearbyEntities(newLoc, 0.4, 0.4, 0.4).forEach(ent -> {
						if (DamageManager.isEnemy(player, ent)) {
							DamageManager.damageEntity(player, ent, DamageReason.MAGIC, 15, false);
							specialEffectsManager.addSpecialEffect(new RootEffect(player, ent, 1.5));
							cancel();
							return;
						}
					});
					newLoc.add(toVec);
				}
			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);
	}

}
