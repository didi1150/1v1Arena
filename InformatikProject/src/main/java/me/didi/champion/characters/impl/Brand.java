package me.didi.champion.characters.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.MageChampion;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.BurnEffect;
import me.didi.utilities.MathUtils;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class Brand extends MageChampion {

	public Brand(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {
		MathUtils.shootProjectile(player, 13, new ItemStack(Material.AIR), 8, true, 0.75,
				new ParticleBuilder(ParticleEffect.FLAME), DamageReason.MAGIC, entity -> {
					DamageManager.damageEntity(player, entity, DamageReason.AUTO, customPlayerManager.getDamage(player),
							true);
					specialEffectsManager.addSpecialEffect(new BurnEffect(player, entity, 4, 3));
					entity.setFireTicks(entity.getFireTicks() + 4 * 19 - 1);
				});
	}
}
