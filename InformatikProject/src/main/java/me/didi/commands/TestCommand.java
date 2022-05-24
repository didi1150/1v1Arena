package me.didi.commands;

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
			int t = 0;
			float increase = 0.2f;
			float radius = 2f;

			Location loc = player.getLocation();
			float y = (float) player.getLocation().getY();

			@Override
			public void run() {
				if (t < 50) {
					float x = radius * (float) Math.sin(t);
					float z = radius * (float) Math.cos(t);
					PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true,
							((float) loc.getX()) + x, (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0,
							1);
					for (Player player : Bukkit.getOnlinePlayers()) {
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
					}
				}
				t += 0.05f;
				y += 0.01;
				radius += increase;
			}

		}, 2, 2);
	}

}
