package me.didi.champion.ability.impl.anakin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.listeners.PlayerMoveListener;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;

public class AnakinSecondAbility implements Ability {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.BLUE + "Force Dash";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.FEATHER)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Anakin quickly dashes into the direction he is moving" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MOVEMENT;
	}

	@Override
	public int getCooldown() {
		return 2;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		abilityStateManager.addCooldown(player, 1, getCooldown());

		Vector vector = player.getLocation().getDirection();
		if (PlayerMoveListener.vectors.containsKey(player)) {
			vector = PlayerMoveListener.vectors.get(player);
		}

		if (player.isOnGround())
			player.setVelocity(vector.normalize().multiply(9).setY(0.2));
		else
			player.setVelocity(vector.normalize().multiply(3).setY(0.1));
	}

}
