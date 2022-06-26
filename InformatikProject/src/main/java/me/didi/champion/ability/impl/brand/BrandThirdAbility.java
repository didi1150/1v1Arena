package me.didi.champion.ability.impl.brand;

import java.awt.Color;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.CurrentStatGetter;
import me.didi.player.effects.BurnEffect;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.Utils;
import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleEffect;

public class BrandThirdAbility implements Ability {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.GOLD + "Conflagration";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(Material.BLAZE_POWDER).setDisplayName(getName()).setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Brand sets the target enemy aflame, which creates a blast",
				ChatColor.GRAY + "that deals " + ChatColor.DARK_AQUA + "magic damage (" + ChatColor.WHITE + "70"
						+ ChatColor.DARK_PURPLE + " (+45% AP)" + ChatColor.DARK_AQUA + ") " + ChatColor.GRAY
						+ "to them and nearby enemies." };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MAGIC;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		abilityStateManager.addCooldown(player, 2, getCooldown());

		Player targetPlayer = Utils.getTargetPlayer(player, 9);

		if (targetPlayer == null)
			return;

		Location dest = targetPlayer.getLocation();

		drawCircle(dest, targetPlayer, specialEffectsManager, player, true);
	}

	private void drawCircle(Location dest, Entity target, SpecialEffectsManager specialEffectsManager, Player player,
			boolean firstTime) {
		TaskManager.getInstance().repeat(1, 1, new Consumer<BukkitTask>() {

			double radius = 5;

			@Override
			public void accept(BukkitTask task) {
				if (radius <= 0) {
					task.cancel();
					burnEntity(target, specialEffectsManager, player);
					if (firstTime) {
						for (Entity entity : target.getNearbyEntities(4, 4, 4)) {
							if (DamageManager.isEnemy(player, entity)) {
								drawCircle(entity.getLocation(), entity, specialEffectsManager, player, false);
							}
						}
					}
					return;
				}
				ParticleUtils.drawCircle(ParticleEffect.REDSTONE, Color.ORANGE, dest, radius);
				radius -= 0.75;
			}

		});
	}

	private void burnEntity(Entity target, SpecialEffectsManager specialEffectsManager, Player player) {
		double damage = CurrentStatGetter.getInstance().getAbilityPower(player) * 0.45 + 70;
		DamageManager.damageEntity(player, target, DamageReason.MAGIC, damage, false);
		specialEffectsManager.addSpecialEffect(new BurnEffect(player, target, 4, 3));
	}
}
