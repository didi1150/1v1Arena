package me.didi.characters;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.ability.AbilityCooldownManager;

public abstract class Champion {

	private Ability[] abilities;

	private String name;

	private ItemStack icon;

	private ItemStack autoAttackItem;

	private int baseHealth;
	private int baseDefense;
	private int baseMagicResist;
	protected Player player;

	protected AbilityCooldownManager abilityCooldownManager;

	public Champion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon, ItemStack autoAttackItem) {
		this.name = name;
		this.abilities = abilities;
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.baseMagicResist = baseMagicResist;
		this.icon = icon;
		this.abilityCooldownManager = MainClass.getPlugin().getAbilityCooldownManager();
		this.autoAttackItem = autoAttackItem;
	}

	public abstract void stopAllTasks();

	public abstract Champion clone();

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public ItemStack getAutoAttackItem() {
		return autoAttackItem;
	}

	/**
	 * Executes the basic AutoAttack <br>
	 * Still on TODO List
	 */
	public abstract void executeAutoAttack();

	/**
	 * Executes the first ability
	 */
	public abstract void executeFirstAbility();

	/**
	 * Executes the second ability
	 */
	public abstract void executeSecondAbility();

	/**
	 * Executes the third ability
	 */
	public abstract void executeThirdAbility();

	/**
	 * Executes the ultimate
	 */
	public abstract void executeUltimate();

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
