package me.didi.ability;

import org.bukkit.inventory.ItemStack;

public class RecastableAbility extends Ability implements Recastable{

	private int recastCooldown;
	
	public RecastableAbility(String name, ItemStack icon, String[] description, int recastCooldown) {
		super(name, icon, description);
		this.recastCooldown = recastCooldown;
	}

	@Override
	public int getRecastCooldown() {
		return recastCooldown;
	}

}
