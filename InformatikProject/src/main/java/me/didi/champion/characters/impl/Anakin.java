package me.didi.champion.characters.impl;

import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.ability.AbilityImpl;
import me.didi.champion.Champion;
import me.didi.champion.characters.MeleeChampion;

public class Anakin extends MeleeChampion {

	public Anakin(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon, ItemStack autoAttackItem) {
		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon, autoAttackItem);
	}

	@Override
	public void executeAutoAttack() {

	}

	@Override
	public void executeFirstAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeSecondAbility() {
//		ChatUtils.broadCastMessage("(" + toVec.getX() + " | " + toVec.getY() + " | " + toVec.getZ() + ")");
//		ChatUtils.broadCastMessage("(" + toVec.getX() * 2 + " | " + toVec.getY() + " | " + toVec.getZ() * 2 + ")");

		player.setVelocity(player.getLocation().getDirection().multiply(2).setY(0.1));

		abilityCooldownManager.addCooldown(player, 1, 2);
	}

	@Override
	public void executeThirdAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeUltimate() {
		// TODO Auto-generated method stub

	}

	@Override
	public Champion clone() {
		return new Anakin(getName(), getAbilities(), getBaseHealth(), getBaseDefense(), getBaseMagicResist(), getIcon(),
				getAutoAttackItem());
	}

	@Override
	public void stopAllTasks() {
		// TODO Auto-generated method stub

	}
}
