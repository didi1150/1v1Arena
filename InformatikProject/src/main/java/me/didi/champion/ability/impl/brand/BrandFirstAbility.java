package me.didi.champion.ability.impl.brand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.player.effects.StunEffect;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.MathUtils;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class BrandFirstAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.GOLD + "Sear";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.FIREBALL).setDisplayName(getName()).setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Brand launches a fireball in the target direction that deals",
				ChatColor.DARK_AQUA + "magic damage" + ChatColor.GRAY + " to the first enemy hit." };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 8;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		MathUtils.shootProjectile(player, 10, new ItemStack(Material.FIREBALL), 10, false, 0.8,
				new ParticleBuilder(ParticleEffect.FLAME), DamageReason.MAGIC, entity -> {
					DamageManager.damageEntity(player, entity, DamageReason.MAGIC, 20, false);
					if (entity.getFireTicks() > 0) {
						specialEffectsManager.addSpecialEffect(new StunEffect(player, entity, 1.5));
						entity.setFireTicks(entity.getFireTicks() + 4 * 19 - 1);
					}
				});
	}

}
