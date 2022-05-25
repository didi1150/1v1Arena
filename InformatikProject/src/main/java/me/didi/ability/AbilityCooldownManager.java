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

	private class RecastCooldown {
		int index;
		int seconds;

		public RecastCooldown(int index, int seconds) {
			this.index = index;
			this.seconds = seconds;
		}

		public int getIndex() {
			return index;
		}

		public int getSeconds() {
			return seconds;
		}

		public void setSeconds(int seconds) {
			this.seconds = seconds;
		}
	}

	private BukkitTask bukkitTask;
	private MainClass plugin;
	private Map<UUID, int[]> cooldowns = new HashMap<>();
	private Map<UUID, RecastCooldown> recasts = new HashMap<>();

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
							itemManager.setItem(player, i, ability.getIcon());
						else if (array[i] > 0) {
							itemManager.setItem(player, i, createOnCooldownItem(array[i], ability.getName()));
							array[i]--;
						}
					}
				}

				for (Map.Entry<UUID, RecastCooldown> entry : recasts.entrySet()) {
					Player player = Bukkit.getPlayer(entry.getKey());
					RecastCooldown recastCooldown = entry.getValue();
					Ability ability = plugin.getChampionsManager().getSelectedChampion(player)
							.getAbilities()[recastCooldown.getIndex()];
					int seconds = recastCooldown.getSeconds();

					if (seconds == 0) {
						removeRecastCooldown(player, ability);
					} else if (seconds > 0) {
						itemManager.setItem(player, recastCooldown.getIndex(),
								new ItemBuilder(ability.getIcon().clone()).setAmount(seconds).toItemStack());
						recastCooldown.setSeconds(seconds - 1);
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

	public void addCooldown(Player player, int index, int cooldown) {
		if (cooldowns.containsKey(player.getUniqueId())) {
			int[] array = cooldowns.get(player.getUniqueId());
			array[index] = cooldown;
		} else {
			int[] array = new int[] { 0, 0, 0, 0 };
			array[index] = cooldown;
			cooldowns.put(player.getUniqueId(), array);
		}
	}

	public void addRecastCooldown(Player player, int index, int cooldown) {
		if (!recasts.containsKey(player.getUniqueId())) {
			RecastCooldown recastCooldown = new RecastCooldown(index, cooldown);
			recasts.put(player.getUniqueId(), recastCooldown);
		}
	}

	public void removeRecastCooldown(Player player, Ability ability) {
		if (recasts.containsKey(player.getUniqueId())) {
			int index = recasts.get(player.getUniqueId()).getIndex();
			recasts.remove(player.getUniqueId());
			itemManager.setItem(player, index, ability.getIcon());
		}
	}

}
