package me.didi.characters;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;

public abstract class Champion {

	private Ability[] abilities;

	private String name;

	private ItemStack icon;

	private int baseHealth;
	private int baseDefense;
	private int baseMagicResist;

	public Champion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon) {
		this.name = name;
		this.abilities = abilities;
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.baseMagicResist = baseMagicResist;
		this.icon = icon;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	/**
	 * Executes the basic AutoAttack <br>
	 * Still on TODO List
	 */
	public abstract void executeAutoAttack();

	/**
	 * Executes the first ability
	 */
	public abstract void executeFirstAbility(Player player);

	/**
	 * Executes the second ability
	 */
	public abstract void executeSecondAbility(Player player);

	/**
	 * Executes the third ability
	 */
	public abstract void executeThirdAbility(Player player);

	/**
	 * Executes the ultimate
	 */
	public abstract void executeUltimate(Player player);

	/**
	 * Returns this champion's base magic resistance
	 */
	public int getBaseMagicResist() {
		return baseMagicResist;
	}

	/**
	 * Returns this champion's base defense
	 */
	public int getBaseDefense() {
		return baseDefense;
	}

	/**
	 * Returns this champion's abilities
	 */
	public int getBaseHealth() {
		return baseHealth;
	}

	public Ability[] getAbilities() {
		return abilities;
	}
}
