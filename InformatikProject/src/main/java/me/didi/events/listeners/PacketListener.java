package me.didi.events.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;

public class PacketListener implements Listener {

	private static PacketListener instance;

	private List<Player> attackSpeedCooldowns = new ArrayList<Player>();

	public static PacketListener getInstance() {
		if (instance == null)
			instance = new PacketListener();
		return instance;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		injectPlayer(event.getPlayer());
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		removePlayer(event.getPlayer());
	}

	private void removePlayer(Player player) {
		Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
		channel.eventLoop().submit(() -> {
			channel.pipeline().remove(player.getName());
			return null;
		});
	}

	private void injectPlayer(Player player) {
		ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

			@Override
			public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
				super.channelRead(channelHandlerContext, packet);
			}

			@Override
			public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise)
					throws Exception {
				if (packet instanceof PacketPlayOutAnimation) {
//					PacketPlayOutAnimation packetPlayOutAnimation = (PacketPlayOutAnimation) packet;
					if (attackSpeedCooldowns.contains(player))
						return;
				}
				super.write(channelHandlerContext, packet, channelPromise);
			}

		};

		ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel
				.pipeline();
		pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);

	}
}
