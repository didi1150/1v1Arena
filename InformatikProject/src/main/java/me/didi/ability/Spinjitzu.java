package me.didi.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Spinjitzu implements Ability {

	
	
	public static AbilityImpl get() {
		return new AbilityImpl(AbilityType.MELEE, "Spinjitzu", new ItemStack(Material.STRING), 10);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbilityType getAbilityType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
