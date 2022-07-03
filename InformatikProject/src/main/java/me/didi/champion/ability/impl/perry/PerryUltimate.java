package me.didi.champion.ability.impl.perry;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemSetter;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleEffect;

public class PerryUltimate implements Ability {

	@Override
	public String getName() {
		return ChatColor.GOLD + "Warrior";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.REDSTONE_LAMP_OFF)).setDisplayName(getName())
				.setLore(getDescription()).toItemStack();
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.GRAY + "Perry hears his colleagues chanting his name, motivating",
				ChatColor.GRAY + "him. This increases his " + ChatColor.GREEN + "baseHealth" + ChatColor.GRAY + ", "
						+ ChatColor.YELLOW + "baseDefense" + ChatColor.GRAY + " and " + ChatColor.AQUA
						+ "magic resistance " + ChatColor.GRAY + "by 70 percent" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MELEE;
	}

	@Override
	public int getCooldown() {
		return 30;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		if (player.getInventory().getItem(3).getEnchantments().isEmpty()) {
			TaskManager.getInstance().repeatUntil(0, 1, 20 * 10, (task, counter) -> {
				for (int radius = 1; radius <= 4; radius++) {
					for (double s = 0; s < 2 * Math.PI * radius; s += (2 * Math.PI / 9)) {
						double t = s + Math.toRadians(radius * 12);
						double x = radius * (Math.cos(t));
						double z = radius * (Math.sin(t));

						Location location = player.getLocation().add(x, 0, z);
						ParticleEffect.REDSTONE.display(location, Color.green);
					}
				}
			});
			new ItemSetter().setItem(player, 3, new ItemBuilder(getIcon().clone()).addGlow().toItemStack());
			CustomPlayer customPlayer = CustomPlayerManager.getInstance().getPlayer(player);
			customPlayer.setBaseDefense(customPlayer.getBaseDefense() * 1.7f);
			customPlayer.setBaseHealth(customPlayer.getBaseHealth() * 1.7f);
			customPlayer.setMagicResist(customPlayer.getMagicResist() * 1.7f);

			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2, false, false));

			TaskManager.getInstance().runTaskLater(20 * 10, task -> {
				abilityStateManager.addCooldown(player, 3, getCooldown());
				customPlayer.setBaseDefense(customPlayer.getBaseDefense() / 1.7f);
				customPlayer.setBaseHealth(customPlayer.getBaseHealth() / 1.7f);
				if (customPlayer.getCurrentHealth() > customPlayer.getBaseHealth()
						+ CustomPlayerManager.getInstance().getBonusHealth(player)) {
					customPlayer.setCurrentHealth(
							customPlayer.getBaseHealth() + CustomPlayerManager.getInstance().getBonusHealth(player));
				}
				customPlayer.setMagicResist(customPlayer.getMagicResist() / 1.7f);
			});
		}
	}

}
