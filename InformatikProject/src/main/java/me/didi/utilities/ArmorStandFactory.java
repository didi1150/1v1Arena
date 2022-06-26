package me.didi.utilities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;

public class ArmorStandFactory {

	
	/**
	 * Spaws an invisible armorstand without arms and base plate
	 * */
	public static Entity spawnInvisibleArmorStand(Location l) {
		// You can remove the net.minecraft.server.v1_8_R3 and just import the classes
		// You need to change v1_8_R3 for your version.
		net.minecraft.server.v1_8_R3.World w = ((CraftWorld) l.getWorld()).getHandle();
		net.minecraft.server.v1_8_R3.EntityArmorStand nmsEntity = new net.minecraft.server.v1_8_R3.EntityArmorStand(w);
		// Yes, yaw goes first here ->
		nmsEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		nmsEntity.setInvisible(true);
		nmsEntity.setArms(false);
		nmsEntity.setBasePlate(false);
		/*
		 * You can make other changes like: nmsEntity.setGravity(false);
		 * nmsEntity.setArms(true); nmsEntity.setBasePlate(false); The methods are very
		 * similiar to the ArmorStand ones in the API
		 */
		w.addEntity(nmsEntity);
		return nmsEntity.getBukkitEntity();
	}

}
