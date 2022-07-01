package me.didi.player;

import java.util.UUID;

public class CustomPlayer {

	private float baseHealth = 0;

	private float baseDefense = 0;

	private float magicResist = 0;

	private float armorPenetration = 0;

	private float magicPenetration = 0;

	private int attackDamage = 0;

	private int abilityPower = 0;

	private float remainingShield = 0;

	private UUID uuid;

	private String name;

	private float currentHealth = 0;

	private float baseAttackSpeed = 0;

	public CustomPlayer(int baseHealth, int baseDefense, int magicResist, int armorPenetration, int magicPenetration,
			int attackDamage, int abilityPower, float baseAttackSpeed, UUID uuid, String name) {
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.magicResist = magicResist;
		this.armorPenetration = armorPenetration;
		this.magicPenetration = magicPenetration;
		this.attackDamage = attackDamage;
		this.abilityPower = abilityPower;
		this.baseAttackSpeed = baseAttackSpeed;
		this.uuid = uuid;
		this.name = name;
		this.currentHealth = baseHealth;
	}

	public float getBaseHealth() {
		return baseHealth;
	}

	public void setBaseHealth(float baseHealth) {
		this.baseHealth = baseHealth;
	}

	public float getBaseDefense() {
		return baseDefense;
	}

	public void setBaseDefense(float baseDefense) {
		this.baseDefense = baseDefense;
	}

	public float getMagicResist() {
		return magicResist;
	}

	public void setMagicResist(float magicResist) {
		this.magicResist = magicResist;
	}

	public float getArmorPenetration() {
		return armorPenetration;
	}

	public void setArmorPenetration(float armorPenetration) {
		this.armorPenetration = armorPenetration;
	}

	public float getMagicPenetration() {
		return magicPenetration;
	}

	public void setMagicPenetration(float magicPenetration) {
		this.magicPenetration = magicPenetration;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(float currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getAbilityPower() {
		return abilityPower;
	}

	public void setAbilityPower(int abilityPower) {
		this.abilityPower = abilityPower;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public float getBaseAttackSpeed() {
		return baseAttackSpeed;
	}

	public void setRemainingShield(float remainingShield) {
		this.remainingShield = remainingShield;
	}

	public float getRemainingShield() {
		return remainingShield;
	}

}
