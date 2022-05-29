package me.didi.utilities;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class ArmorStandFactory {

	public static ArmorStand buildArmorStand(Location spawnLocation, boolean gravity, boolean visible) {
//		World world = ((CraftWorld) spawnLocation.getWorld()).getHandle();
//		EntityArmorStand armorStand = new EntityArmorStand(world);
//		armorStand.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(),
//				spawnLocation.getPitch());
//
//		armorStand.setGravity(noGravity);
//		armorStand.setInvisible(invisible);
//
//		armorStand.spawnIn(world);
//		return (ArmorStand) armorStand.getBukkitEntity();
//
		ArmorStand armStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
		armStand.setVisible(visible);
		armStand.setGravity(gravity);

		return armStand;
	}

}
