package me.didi.characters;

import org.bukkit.entity.Player;

import me.didi.ability.Ability;

public abstract class Champion {

	private Ability[] abilities;

	private Player player;
	
	private int baseHealth;
	private int baseDefense;
	private int baseMagicResist;

	public Champion(Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist) {
		this.abilities = abilities;
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.baseMagicResist = baseMagicResist;
	}

	/**
	 * Sets this champion's user
	 * */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Executes the basic AutoAttack
	 * <br> Still on TODO List
	 */
	public abstract void executeAutoAttack();

	/**
	 * Executes an ability
	 */
	public abstract void executeAbility(int index);

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
