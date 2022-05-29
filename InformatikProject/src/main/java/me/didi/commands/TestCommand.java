package me.didi.commands;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.didi.MainClass;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
//			airjitzu(player);
			createSphere(player);
		}
		return true;
	}

	private void airjitzu(final Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.getPlugin(), new Runnable() {
			@Override
			public void run() {
				createSphere(player);
				cyclone(player);
			}

		}, 20, 20);
	}

	private void spin(Player player) {
		float increase = 0.002f;
		float radius = 0f;

		Location loc = player.getLocation().clone();
		float y = (float) loc.getY() - 0.5f;

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

	@SuppressWarnings("deprecation")
	private void cyclone(Player player) {
		int max_height = 3;
		double max_radius = 2;
		int lines = 20;
		double height_increasement = 0.1;
		double radius_increasement = max_radius / max_height;

		Location loc = player.getLocation().subtract(0, 0.5, 0);
		for (int angle = 0; angle < 360; angle++) {
			for (int l = 0; l < lines; l++) {
				for (double y = 0; y < max_height; y += height_increasement) {
					double radius = y * radius_increasement;
					double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
					double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;

					Location next = loc.clone().add(x, y, z);
					player.playEffect(next, Effect.HAPPY_VILLAGER, 0);
//				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true,
//						(float) next.getX(), (float) next.getY(), (float) next.getZ(), 0, 0, 0, 0, 1);)
//				for (Player pl : Bukkit.getOnlinePlayers()) {
//					((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
//				}
				}
			}
		}
	}

	public void createSphere(Player player) {
		Location location = player.getLocation().add(0, 1, 0);
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double radius = Math.sin(i);
			double y = Math.cos(i);
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				double x = Math.cos(a) * radius;
				double z = Math.sin(a) * radius;
				location.add(x, y, z);
				// display particle at 'location'.
				player.playEffect(location, Effect.HAPPY_VILLAGER, 0);
				location.subtract(x, y, z);
			}
		}
	}

}
