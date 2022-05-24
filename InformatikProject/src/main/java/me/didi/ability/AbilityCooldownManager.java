package me.didi.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.didi.MainClass;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemManager;
import net.md_5.bungee.api.ChatColor;

public class AbilityCooldownManager {

	private BukkitTask bukkitTask;
	private MainClass plugin;
	private Map<UUID, int[]> cooldowns = new HashMap<>();

	private ItemManager itemManager;

	public AbilityCooldownManager(MainClass plugin, ItemManager itemManager) {
		this.plugin = plugin;
		this.itemManager = itemManager;
	}

	public void startBackGroundTask() {
		bukkitTask = new BukkitRunnable() {

			@Override
			public void run() {
				for (Map.Entry<UUID, int[]> entry : cooldowns.entrySet()) {
					Player player = Bukkit.getPlayer(entry.getKey());
					int[] array = entry.getValue();
					for (int i = 0; i < array.length; i++) {
						Ability ability = plugin.getChampionsManager().getSelectedChampion(player).getAbilities()[i];
						if (array[i] == 0)
							continue;

						if (array[i] - 1 > 0) {
							itemManager.setItem(player, i, createOnCooldownItem(array[i], ability.getName()));
							array[i]--;
						} else {
							array[i]--;
							itemManager.setItem(player, i, ability.getIcon());
						}
					}
				}
			}
		}.runTaskTimer(plugin, 20, 20);
	}

	public void stopBackgroundTask() {
		bukkitTask.cancel();
	}

	/**
	 * Creates the item, which is displayed if there is still cooldown left
	 */
	private ItemStack createOnCooldownItem(int cooldownLeft, String abilityName) {
		return new ItemBuilder(new ItemStack(Material.INK_SACK, cooldownLeft, (short) 1)).setDisplayName(abilityName)
				.setLore(ChatColor.GRAY + "This Ability is", ChatColor.GRAY + "on cooldown!").toItemStack();
	}

}
