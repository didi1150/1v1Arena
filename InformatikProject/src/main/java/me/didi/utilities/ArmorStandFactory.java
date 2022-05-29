package me.didi.utilities;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;

public class ArmorStandFactory {

	public static ArmorStand buildArmorStand(Location spawnLocation, boolean noGravity, boolean invisible) {
		World world = ((CraftWorld) spawnLocation.getWorld()).getHandle();
		EntityArmorStand armorStand = new EntityArmorStand(world);
		armorStand.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(),
				spawnLocation.getPitch());

		armorStand.setGravity(noGravity);
		armorStand.setInvisible(invisible);

		armorStand.spawnIn(world);
		return (ArmorStand) armorStand.getBukkitEntity();
	}

}
