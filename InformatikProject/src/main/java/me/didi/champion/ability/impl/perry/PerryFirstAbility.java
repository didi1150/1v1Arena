package me.didi.champion.ability.impl.perry;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.CurrentStatGetter;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ArmorStandFactory;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import net.md_5.bungee.api.ChatColor;

public class PerryFirstAbility implements Ability {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.DARK_GREEN + "Swift throw";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.LEATHER_HELMET)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Perry throws his hat like a boomerang",
				ChatColor.GRAY + "dealing " + ChatColor.RED + "physical damage (" + ChatColor.WHITE + "40"
						+ ChatColor.GOLD + " (+30.5% AD)" + ChatColor.RED + ")" + ChatColor.GRAY + " on its way" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.RANGED;
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
		abilityStateManager.addCooldown(player, 0, getCooldown());
		shootBoomerang(player);
	}

	private void shootBoomerang(Player player) {
		Location destination = player.getLocation().add(player.getLocation().getDirection().multiply(10)).add(0, 0.5,
				0);

		ArmorStand armorStand = (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(player.getLocation());
		armorStand.setMarker(true);
		armorStand.setGravity(false);
		armorStand.setArms(true);
		armorStand.setItemInHand(new ItemStack(Material.LEATHER_HELMET));
		armorStand.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(120), Math.toRadians(0)));

		long distance = 10;

		List<Entity> hitEntities = new ArrayList<>();
		Location location = player.getLocation();

		TaskManager.getInstance().repeatUntil(1, 1, 20, (task, counter) -> {

			Vector vector = destination.clone().subtract(location).toVector().multiply(0.25).normalize();
			EulerAngle rotation = armorStand.getRightArmPose();
			EulerAngle addRotation = rotation.add(0, 20, 0);

			armorStand.setRightArmPose(addRotation);

			if (counter.get() >= distance) {
				vector = player.getLocation().add(0, 0.5, 0).toVector().subtract(armorStand.getLocation().toVector())
						.normalize();
				hitEntities.clear();
				armorStand.teleport(armorStand.getLocation().add(vector));

				if (counter.get() >= distance * 2) {
					armorStand.remove();
					task.cancel();
				}
			} else {
				armorStand.teleport(armorStand.getLocation().add(vector));
			}

			for (Entity entity : armorStand.getWorld().getNearbyEntities(armorStand.getLocation().add(0, 1, 0), 1, 1,
					1)) {
				if (DamageManager.isEnemy(player, entity) && !hitEntities.contains(entity)) {
					double damage = CurrentStatGetter.getInstance().getAttackDamage(player) * 0.305 + 40;
					hitEntities.add(entity);
					DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, damage, false);
				}
			}
		});
	}

}
