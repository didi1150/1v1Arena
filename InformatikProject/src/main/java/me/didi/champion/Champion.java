package me.didi.champion;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
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
	private int baseArmorPenetration;
	private int baseMagicPenetration;
	private int baseAttackDamage;
	private int baseAbilityPower;

	protected Player player;

	protected MainClass plugin;

	protected AbilityStateManager abilityStateManager;

	protected DamageManager damageManager;

	protected SpecialEffectsManager specialEffectsManager;

	protected CustomPlayerManager customPlayerManager;

	public Champion(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			int baseAttackDamage, int baseAbilityPower, int baseArmorPenetration, int baseMagicPenetration,
			ItemStack icon, ItemStack autoAttackItem) {
		this.name = name;
		this.abilities = abilities;
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.baseMagicResist = baseMagicResist;
		this.baseAttackDamage = baseAttackDamage;
		this.baseAbilityPower = baseAbilityPower;
		this.baseArmorPenetration = baseArmorPenetration;
		this.baseMagicPenetration = baseMagicPenetration;
		this.icon = icon;
		this.autoAttackItem = autoAttackItem;
		this.damageManager = new DamageManager();
	}

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
		this.abilityStateManager = abilityCooldownManager;
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

	/**
	 * Returns this champion's base armor penetration
	 */
	public int getBaseArmorPenetration() {
		return baseArmorPenetration;
	}

	/**
	 * Returns this champion's base armor penetration
	 */
	public int getBaseMagicPenetration() {
		return baseMagicPenetration;
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

	public int getBaseAttackDamage() {
		return baseAttackDamage;
	}

	public int getBaseAbilityPower() {
		return baseAbilityPower;
	}
}
