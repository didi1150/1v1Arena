package me.didi.champion.ability.impl.rex;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.CurrentStatGetter;
import me.didi.player.effects.RootEffect;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.Utils;
import xyz.xenondevs.particle.ParticleBuilder;
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
				ChatColor.GRAY + "causing the first enemy hit to be " + ChatColor.YELLOW + ChatColor.ITALIC + "rooted",
				ChatColor.GRAY + " for 1.5 seconds and dealing " + ChatColor.DARK_AQUA + "magic damage (" + ChatColor.WHITE
						+ "20" + ChatColor.DARK_PURPLE + " (+60% AP)" + ChatColor.DARK_AQUA + ")" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		abilityStateManager.addCooldown(player, 2, getCooldown());

		double damage = 20 + CurrentStatGetter.getInstance().getAbilityPower(player) * 0.6;
		Utils.shootProjectile(player, 13, new ItemStack(Material.CLAY_BALL), damage, false, 0.5,
				new ParticleBuilder(ParticleEffect.REDSTONE).setColor(java.awt.Color.CYAN), DamageReason.MAGIC,
				new Consumer<Entity>() {

					@Override
					public void accept(Entity entity) {
						specialEffectsManager.addSpecialEffect(new RootEffect(player, entity, 1.5));
						return;
					}
				});

	}

}
