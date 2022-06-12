package me.didi.champion.characters.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.characters.RangedChampion;
import me.didi.utilities.MathUtils;

public class Rex extends RangedChampion {

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {
		MathUtils.shootAutoAttackProjectile(player, 13, new ItemStack(Material.PRISMARINE_CRYSTALS),
				customPlayerManager.getDamage(player), true, 0.75);
	}

	@Override
	public void executeFirstAbility() {
		getAbilities()[0].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeSecondAbility() {
		getAbilities()[1].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeThirdAbility() {

		getAbilities()[2].execute(abilityStateManager, player, specialEffectsManager);
	}

	@Override
	public void executeUltimate() {

		getAbilities()[3].execute(abilityStateManager, player, specialEffectsManager);
	}
}
