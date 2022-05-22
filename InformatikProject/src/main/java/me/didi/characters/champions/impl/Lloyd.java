package me.didi.characters.champions.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.champions.MeleeChampion;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Lloyd extends MeleeChampion {

	public Lloyd(String name, Ability[] abilities, int baseHealth, int baseDefense, int baseMagicResist,
			ItemStack icon) {

		super(name, abilities, baseHealth, baseDefense, baseMagicResist, icon);
		// TODO: ItemStack builder
//		Ability firstAbility = new OneTimeAbility("Shurikens",
//				new ItemBuilder(new ItemStack(Material.INK_SACK, (short) 15))
//						.setDisplayName(ChatColor.GOLD + "Shurikens")
//						.setLore(ChatColor.GRAY + "Throws out shurikens",
//								ChatColor.GRAY + "dealing " + ChatColor.RED + "60" + ChatColor.GRAY + " damage.")
//						.toItemStack());
//		Ability secondAbility = new OneTimeAbility("Disguise", null);
//
//		Ability thirdAbility = new OneTimeAbility("Blind", null);
//
//		RecastableAbility ultimateAbility = new RecastableAbility("Beyblade", null);
//		ultimateAbility.addFunction(new Callable<Player>() {
//
//			@Override
//			public Player call() throws Exception {
//				return getPlayer();
//			}
//		});
	}

	@Override
	public void executeAutoAttack() {
		// TODO
	}

	@Override
	public void executeFirstAbility(Player player) {
		
	}

	@Override
	public void executeSecondAbility(Player player) {
		
	}

	@Override
	public void executeThirdAbility(Player player) {
		
	}

	@Override
	public void executeUltimate(Player player) {
		int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(null, new Runnable() {
			int t = 0;
			float increase = 0.2f;
			float radius = 2f;

			Location loc = getPlayer().getLocation();
			float y = (float) getPlayer().getLocation().getY();

			@Override
			public void run() {
				if (t < 50) {
					float x = radius * (float) Math.sin(t);
					float z = radius * (float) Math.cos(t);
					PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME,
							true, ((float) loc.getX()) + x, (float) (loc.getY() + y), (float) (loc.getZ() + z),
							0, 0, 0, 0, 1);
					for (Player player : Bukkit.getOnlinePlayers()) {
						((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
					}
				} else {
					t = 0;
				}
				t += 0.05f;
				y += 0.01;
				radius += increase;
			}

		}, 2, 2);
	}
}
