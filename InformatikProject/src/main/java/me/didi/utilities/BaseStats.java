package me.didi.utilities;

public class BaseStats {

	private int baseHealth = 0;
	private int baseDefense = 0;
	private int baseMagicResist = 0;
	private int baseArmorPenetration = 0;
	private int baseMagicPenetration = 0;
	private int baseAttackDamage = 0;
	private int baseAbilityPower = 0;
	private int attackSpeed = 0;

	public BaseStats(int baseHealth, int baseDefense, int baseMagicResist, int baseArmorPenetration,
			int baseMagicPenetration, int baseAttackDamage, int baseAbilityPower, int attackSpeed) {
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.baseMagicResist = baseMagicResist;
		this.baseArmorPenetration = baseArmorPenetration;
		this.baseMagicPenetration = baseMagicPenetration;
		this.baseAttackDamage = baseAttackDamage;
		this.baseAbilityPower = baseAbilityPower;
		this.attackSpeed = attackSpeed;
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public int getBaseDefense() {
		return baseDefense;
	}

	public int getBaseMagicResist() {
		return baseMagicResist;
	}

	public int getBaseArmorPenetration() {
		return baseArmorPenetration;
	}

	public int getBaseMagicPenetration() {
		return baseMagicPenetration;
	}

	public int getBaseAttackDamage() {
		return baseAttackDamage;
	}

	public int getBaseAbilityPower() {
		return baseAbilityPower;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

}
