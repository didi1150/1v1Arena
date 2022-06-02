package me.didi.commands;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.didi.MainClass;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import xyz.xenondevs.particle.ParticleEffect;

public class TestCommand implements CommandExecutor {

	static double[] x2Array = new double[27720];
	static double[] z2Array = new double[27720];
	static double[] y2Array = new double[27720];

	static double[] x3Array = new double[220];
	static double[] z3Array = new double[220];
	static double[] y3Array = new double[220];

	static {
		int max_height = 2;
		double max_radius = 2;
		int lines = 7;
		double height_increasement = 0.2;
		double radius_increasement = max_radius / max_height;

		int counter = 0;
		for (int angle = 0; angle < 360; angle++) {
			for (int l = 0; l < lines; l++) {
				for (double y = 0; y < max_height; y += height_increasement) {
					double radius = y * radius_increasement;
					double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
					double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;

					x2Array[counter] = x;
					z2Array[counter] = z;
					y2Array[counter] = y;
					counter++;
				}
			}
		}

		counter = 0;
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
			double radius = Math.sin(i);
			double y = Math.cos(i);
			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
				double x = Math.cos(a) * radius;
				double z = Math.sin(a) * radius;

				x3Array[counter] = x;
				z3Array[counter] = z;
				y3Array[counter] = y;

				counter++;
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
//			airjitzu(player);
			cyclone(player);
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

	private void cyclone(Player player) {
//		int max_height = 2;
//		double max_radius = 2;
//		int lines = 7;
//		double height_increasement = 0.2;
//		double radius_increasement = max_radius / max_height;
//
		Location loc = player.getLocation().subtract(0, 1.5, 0);
//		int counter = 0;
//		for (int angle = 0; angle < 360; angle++) {
//			for (int l = 0; l < lines; l++) {
//				for (double y = 0; y < max_height; y += height_increasement) {
//					double radius = y * radius_increasement;
//					double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
//					double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
//
//					Location next = loc.clone().add(x, y, z);
//					ParticleEffect.REDSTONE.display(next, Color.GREEN);
//					counter++;
//				}
//			}
//		}

		for (int i = 0; i < x2Array.length; i++) {
			double x = x2Array[i];
			double z = z2Array[i];
			double y = y2Array[i];
			Location next = loc.clone().add(x, y, z);
			ParticleEffect.REDSTONE.display(next, Color.GREEN);

		}
	}

	public void createSphere(Player player) {
		Location location = player.getLocation().add(0, 1, 0);
//		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
//			double radius = Math.sin(i);
//			double y = Math.cos(i);
//			for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
//				double x = Math.cos(a) * radius;
//				double z = Math.sin(a) * radius;
//				location.add(x, y, z);
//				// display particle at 'location'.
//				ParticleEffect.REDSTONE.display(location, Color.GREEN);
//				location.subtract(x, y, z);
//			}
//		}

		for (int i = 0; i < x3Array.length; i++) {
			double x = x3Array[i];
			double z = z3Array[i];
			double y = y3Array[i];
			Location next = location.clone().add(x, y, z);
			ParticleEffect.REDSTONE.display(next, Color.GREEN);

		}
	}

}
