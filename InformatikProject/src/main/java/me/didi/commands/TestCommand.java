package me.didi.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			airjitzu(player);
		}
		return true;
	}

	private void airjitzu(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getPlugin(), new Runnable() {
			@Override
			public void run() {
				spin(player);
			}

		}, 2, 2);
	}

	private void spin(Player player) {
		float increase = 0.002f;
		float radius = 0f;

		Location loc = player.getLocation();
		float y = (float) player.getLocation().getY() - 0.5f;

		for (double t = 0; t <= 50; t += 0.05f) {
			float x = radius * (float) Math.sin(t);
			float z = radius * (float) Math.cos(t);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true,
					((float) loc.getX()) + x, y, (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
			for (Player pl : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
			}
			y += 0.003f;
			radius += increase;
		}
	}
//
//	private void cyclone() {
//		int max_height = 15;
//		double max_radius = 10;
//		int lines = 4;
//		double height_increasement = 0.5;
//		double radius_increasement = max_radius / max_height;
//		for (int l = 0; l < lines; l++) {
//			for (double y = 0; y < max_height; y += height_increasement) {
//				double radius = y * radius_increasement;
//				double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
//				double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
//				ParticleEffect.CLOUD.display(0, 0, 0, 1, 1, location.clone().add(x, y, z), 255);
//			}
//		}
//		angle++;
//	}

}
