package me.didi.champion.ability.impl.perry;

import java.awt.Color;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.MathUtils;
import me.didi.utilities.ParticleUtils;
import xyz.xenondevs.particle.ParticleEffect;

public class PerryThirdAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.AQUA + "PowerSHOT";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.DIAMOND_BARDING)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Perry overloads his blaster, causing it",
				ChatColor.GRAY + "to fire off a powerful shot, knocking back enemies",
				ChatColor.GRAY + "and dealing" + ChatColor.RED + " 30 damage" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.RANGED;
	}

	@Override
	public int getCooldown() {
		return 15;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		abilityStateManager.addCooldown(player, 2, getCooldown());
		ParticleUtils.createSphere(ParticleEffect.REDSTONE, Color.CYAN, player.getLocation().add(0, 1, 0), 1);
		MathUtils.shootProjectile(player, 9, new ItemStack(Material.BEACON), 30, false, 0.8, null, DamageReason.MAGIC,
				new Consumer<Entity>() {

					@Override
					public void accept(Entity entity) {
						Vector vector = player.getLocation().getDirection().multiply(10);
						entity.setVelocity(vector.setY(0.3));
					}
				});

	}

}
