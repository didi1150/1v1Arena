package me.didi.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.didi.utilities.BaseStats;

public abstract class CustomItem {

	public abstract BaseStats getBaseStats();

	public abstract ItemStack getItemStack();

	protected int slot = 0;

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getSlot() {
		return slot;
	}

	protected List<ItemPassive> itemPassives;

	public CustomItem(List<ItemPassive> itemPassives) {
		this.itemPassives = itemPassives;
	}

	public List<ItemPassive> getItemPassives() {
		return itemPassives;
	}

	public List<String> getLore() {
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
			lore.add(ChatColor.GRAY + "attack speed: " + ChatColor.GOLD + "+" + attackSpeed + "%");

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

		if (!itemPassives.isEmpty()) {
			lore.add(" ");
			for (ItemPassive itemPassive : itemPassives) {
				for (String string : itemPassive.getDescription())
					lore.add(string);
			}
		}
		return lore;
	}

	public abstract CustomItem clone();

}
