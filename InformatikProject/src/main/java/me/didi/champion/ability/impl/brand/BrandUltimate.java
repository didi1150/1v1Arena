package me.didi.champion.ability.impl.brand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.player.effects.SpecialEffectsManager;

public class BrandUltimate implements Ability {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.GOLD + "Pyroclasm";
	}

	@Override
	public ItemStack getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getDescription() {
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

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

	}

}
