package me.didi.player;

import java.util.UUID;

public class CustomPlayer {

	private int baseHealth;

	private int baseDefense;

	private int magicResist;

	private int armorPenetration;

	private int magicPenetration;

	private UUID uuid;

	private String name;

	public CustomPlayer(int baseHealth, int baseDefense, int magicResist, int armorPenetration, int magicPenetration,
			UUID uuid, String name) {
		this.baseHealth = baseHealth;
		this.baseDefense = baseDefense;
		this.magicResist = magicResist;
		this.armorPenetration = armorPenetration;
		this.magicPenetration = magicPenetration;
		this.uuid = uuid;
		this.name = name;
	}

	public int getArmorPenetration() {
		return armorPenetration;
	}

	public void setArmorPenetration(int armorPenetration) {
		this.armorPenetration = armorPenetration;
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public int getBaseDefense() {
		return baseDefense;
	}

	public int getMagicResist() {
		return magicResist;
	}

	public int getMagicPenetration() {
		return magicPenetration;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}
}
