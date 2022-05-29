package me.didi.characters.champions.impl;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.MeleeChampion;

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
		Location dest = player.getLocation().add(player.getLocation().getDirection().multiply(12));
		Vector toVec = dest.subtract(player.getLocation()).toVector().normalize();
		player.setVelocity(toVec);
		abilityCooldownManager.addCooldown(player, 1, 1);
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
