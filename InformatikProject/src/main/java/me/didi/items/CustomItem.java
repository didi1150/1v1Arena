package me.didi.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.didi.utilities.BaseStats;

public abstract class CustomItem implements ItemPassive {

	public abstract BaseStats getBaseStats();

	public abstract ItemStack getItemStack();

	// TODO
	public List<String> addStatsToLore() {
		List<String> lore = new ArrayList<String>();
		BaseStats baseStats = getBaseStats();
		
		int attackDamage = baseStats.getBaseAttackDamage();
		int abilityPower = baseStats.getBaseAbilityPower();

		int attackSpeed = baseStats.getAttackSpeed();
		
		int armorPenetration = baseStats.getBaseArmorPenetration();
		int magicPenetration = baseStats.getBaseMagicPenetration();
		
		int defense = baseStats.getBaseDefense();
		int magicResist = baseStats.getBaseMagicResist();
		
		int health = baseStats.getBaseHealth();
		
		

		if (attackDamage != 0)
			lore.add(ChatColor.GRAY + "attack damage: " + ChatColor.GOLD + "+" + attackDamage);
		
		if (abilityPower != 0)
			lore.add(ChatColor.GRAY + "ability power: " + ChatColor.DARK_PURPLE + "+" + abilityPower);

		if (attackSpeed != 0)
			lore.add(ChatColor.GRAY + "attack speed: " + ChatColor.GOLD + "+" + attackSpeed);

		if (armorPenetration != 0)
			lore.add(ChatColor.GRAY + "armor penetration: " + ChatColor.GOLD + "+" + armorPenetration);

		if (magicPenetration != 0)
			lore.add(ChatColor.GRAY + "magic penetration: " + ChatColor.GOLD + "+" + magicPenetration);

		if (defense != 0)
			lore.add(ChatColor.GRAY + "defense: " + ChatColor.GREEN + "+" + defense);
		
		if (magicResist != 0)
			lore.add(ChatColor.GRAY + "magic resist: " + ChatColor.AQUA + "+" + magicResist);
		
		if (health != 0)
			lore.add(ChatColor.GRAY + "health: " + ChatColor.GREEN + "+" + health);
		
		return lore;
	}

}
