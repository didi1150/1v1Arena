package me.didi.champion.ability.impl.brand;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;

public class BrandFirstAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.GOLD + "Sear";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.FIREBALL).setDisplayName(getName()).setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Brand launches a fireball in the target direction that deals",
				ChatColor.DARK_AQUA + "magic damage" + ChatColor.GRAY + " to the first enemy hit." };
	}

	@Override
	public AbilityType getAbilityType() {
		return null;
	}

	@Override
	public int getCooldown() {
		return 0;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {

	}

}
