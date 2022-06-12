package me.didi.champion.ability.impl.rex;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ArmorStandFactory;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;
import me.didi.utilities.MathUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import xyz.xenondevs.particle.ParticleEffect;

public class RexSecondAbility implements Ability {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.DARK_GREEN + "Greenade";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_BOMB)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Rex throws a greenade which",
				ChatColor.GRAY + " flies a short distance, exploding", ChatColor.GRAY + "in a small radius",
				ChatColor.GRAY + "dealing " + ChatColor.RED + "20 damage " + ChatColor.GRAY + "to nearby enemies" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.RANGED;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		abilityStateManager.addCooldown(player, 1, getCooldown());
		throwBomb(player);
	}

	private void throwBomb(Player player) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation(entityPlayer, 0);
		entityPlayer.playerConnection.sendPacket(packet);
		ArmorStand as = (ArmorStand) ArmorStandFactory
				.spawnInvisibleArmorStand(MathUtils.getLocationToRight(player.getLocation().add(0, 1, 0), 0.3));
		as.setItemInHand(ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_BOMB));
		Location dest = player.getLocation().clone().add(player.getLocation().getDirection().multiply(25));
		as.setVelocity(MathUtils.calculateVelocity(as.getLocation().toVector(), dest.toVector(), 2));
		new BukkitRunnable() {

			@Override
			public void run() {
				if (as.getWorld().getBlockAt(as.getLocation().add(0, -0.5, 0)).getType() != Material.AIR) {

					as.remove();

					ParticleEffect.EXPLOSION_NORMAL.display(as.getLocation());
					ParticleEffect.EXPLOSION_HUGE.display(as.getLocation());

					as.getWorld().playSound(as.getLocation(), Sound.EXPLODE, 5, 5);

					as.getNearbyEntities(3, 3, 3).forEach(ent -> {
						if (DamageManager.isEnemy(player, ent))
							DamageManager.damageEntity(player, ent, DamageReason.PHYSICAL, 20, true);
					});

					this.cancel();
				}
			}
		}.runTaskTimer(MainClass.getPlugin(), 1, 1);
	}

}
