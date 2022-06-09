package me.didi.champion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityImpl;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.events.customEvents.DamageManager;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;

public abstract class Champion {

	private Ability[] abilities;

	private String name;

	private ItemStack icon;

	private ItemStack autoAttackItem;

	private int baseHealth;
	private int baseDefense;
	private int baseMagicResist;
	protected Player player;

	protected MainClass plugin;

	protected List<ArmorStand> proj;

	protected AbilityStateManager abilityCooldownManager;

	protected DamageManager damageManager;

	protected SpecialEffectsManager specialEffectsManager;

	protected CustomPlayerManager customPlayerManager;

	public Champion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon, ItemStack autoAttackItem) {
		this.name = name;
		this.abilities = abilities;
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.baseMagicResist = baseMagicResist;
		this.icon = icon;
		this.autoAttackItem = autoAttackItem;
		this.damageManager = new DamageManager();
		this.proj = new ArrayList<ArmorStand>();
	}

	public abstract void stopAllTasks();

	public abstract Champion clone();

	public void setCustomPlayerManager(CustomPlayerManager customPlayerManager) {
		this.customPlayerManager = customPlayerManager;
	}

	public void setPlugin(MainClass plugin) {
		this.plugin = plugin;
	}

	public void setSpecialEffectsManager(SpecialEffectsManager specialEffectsManager) {
		this.specialEffectsManager = specialEffectsManager;
	}

	public void setAbilityCooldownManager(AbilityStateManager abilityCooldownManager) {
		this.abilityCooldownManager = abilityCooldownManager;
	}

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

	protected boolean isEnemy(Entity hit) {
		if (hit == player)
			return false;
		if (hit instanceof LivingEntity && !(hit instanceof ArmorStand))
			return true;
		return false;
	}
}
