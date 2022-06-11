package me.didi.champion.ability.impl.anakin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import me.didi.utilities.MathUtils;

public class AnakinThirdAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.RED + "Force Choke";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.WORKBENCH)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Anakin force chokes the enemy, holding",
				ChatColor.GRAY + "them up in the air for 10 seconds. Enemies",
				ChatColor.GRAY + "recieve " + ChatColor.GREEN + "3 damage" + ChatColor.GRAY + " every second" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 15;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		abilityStateManager.addCooldown(player, 2, getCooldown());

		Player target = MathUtils.getTargetPlayer(player, 10);

		if (target == null)
			return;

		Location top = target.getLocation().add(0, 1, 0);

		Vector vector = top.clone().subtract(target.getLocation()).toVector().normalize().multiply(0.005);

		TaskManager.getInstance().repeatUntil(0, 1, 20 * 10, (task, counter) -> {
			target.setAllowFlight(true);
			target.setVelocity(vector);

			if (counter.get() % 20 == 0) {
				DamageManager.damageEntity(player, target, DamageReason.MAGIC, 3, false);
			}

			if (counter.get() >= 20 * 10) {
				target.setAllowFlight(false);
			}

		});
	}

}
