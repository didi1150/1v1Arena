package me.didi.champion.characters.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.characters.RangedChampion;
import me.didi.events.customEvents.DamageReason;
import me.didi.utilities.MathUtils;

public class Rex extends RangedChampion {

	private List<Player> cooldowns = new ArrayList<>();

	public Rex(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist, ItemStack icon,
			ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {
		if (cooldowns.contains(player))
			return;
		cooldowns.add(player);
		Bukkit.getScheduler().runTaskLater(MainClass.getPlugin(), new Runnable() {

			@Override
			public void run() {
				cooldowns.remove(player);
			}
		}, 20 / 4);

		MathUtils.shootArmorStandProjectile(player, 13, new ItemStack(Material.PRISMARINE_CRYSTALS),
				customPlayerManager.getDamage(player), DamageReason.AUTO, true, 0.75);
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
