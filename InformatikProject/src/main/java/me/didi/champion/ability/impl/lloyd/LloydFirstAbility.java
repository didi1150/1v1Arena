package me.didi.champion.ability.impl.lloyd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ArmorStandFactory;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import me.didi.utilities.MathUtils;

public class LloydFirstAbility implements Ability {

	@Override
	public String getName() {
		return ChatColor.GREEN + "Flying daggers";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.IRON_SWORD)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Lloyd sends out 5 daggers",
				ChatColor.GRAY + "dealing " + ChatColor.RED + "10 damage " + ChatColor.GRAY + "each" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MELEE;
	}

	@Override
	public int getCooldown() {
		return 5;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		abilityStateManager.addCooldown(player, 0, getCooldown());

		World world = player.getWorld();

		Location mid = player.getLocation().add(player.getLocation().getDirection().multiply(7));
		mid.setY(player.getLocation().getY() - 1);

		Location location1 = MathUtils.getLocationToLeft(mid.clone(), 2);

		Location location2 = MathUtils.getLocationToLeft(mid, 1);

		Location location3 = MathUtils.getLocationToRight(mid.clone(), 1);

		Location location4 = MathUtils.getLocationToRight(mid.clone(), 2);

		Location[] locations = new Location[] { location2, location1, mid, location3, location4 };

		List<ArmorStand> armorStands = new ArrayList<>();

		Vector[] vectors = new Vector[5];

		Map<Integer, List<Entity>> hitEntities = new HashMap<>();

		for (int i = 0; i < locations.length; i++) {
			Location spawnLocation = MathUtils.getLocationToRight(
					player.getEyeLocation().subtract(player.getEyeLocation().getDirection().normalize().setY(1)), 0.1);
			ArmorStand armorStand = (ArmorStand) ArmorStandFactory.spawnInvisibleArmorStand(spawnLocation);
			armorStand.setSmall(true);
			armorStand.setGravity(false);
			armorStand.setMarker(true);
			armorStand.setRightArmPose(new EulerAngle(Math.toRadians(90), Math.toRadians(90), Math.toRadians(10)));
			armorStand.setItemInHand(new ItemStack(Material.IRON_SWORD));
			armorStands.add(armorStand);

			vectors[i] = locations[i].clone().subtract(spawnLocation).toVector().normalize();
			hitEntities.put(i, new ArrayList<Entity>());
		}

		TaskManager.getInstance().repeat(0, 1, task -> {
			for (int i = 0; i < armorStands.size(); i++) {

				ArmorStand armorStand = armorStands.get(i);
				if (armorStands.size() == 0) {
					task.cancel();
					return;
				}

				if (world.getBlockAt(MathUtils.getLocationToRight(armorStand.getLocation().add(0, 0.6, 0), 0.3))
						.getType() != Material.AIR) {
					armorStands.remove(i);
					armorStand.remove();
				}

				armorStand.teleport(armorStand.getLocation().add(vectors[i].clone()));

				for (Entity entity : armorStand.getNearbyEntities(1.5, 3, 1.5)) {
					if (DamageManager.isEnemy(player, entity) && !hitEntities.get(i).contains(entity)) {
						hitEntities.get(i).add(entity);
						DamageManager.damageEntity(player, entity, DamageReason.PHYSICAL, 10, false);
					}
				}

			}
		});

	}

}
