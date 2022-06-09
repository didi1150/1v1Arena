package me.didi.champion.ability;

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
import me.didi.champion.ChampionsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemManager;
import net.md_5.bungee.api.ChatColor;

public class AbilityStateManager {

	private BukkitTask bukkitTask;
	private MainClass plugin;
	private Map<UUID, AbilityState> abilityStates = new HashMap<UUID, AbilityState>();

	private ChampionsManager championsManager;
	private ItemManager itemManager;

	public AbilityStateManager(MainClass plugin, ChampionsManager championsManager, ItemManager itemManager) {
		this.championsManager = championsManager;
		this.plugin = plugin;
		this.itemManager = itemManager;
	}

	public void startBackGroundTask() {
		bukkitTask = new BukkitRunnable() {

			@Override
			public void run() {
				for (Map.Entry<UUID, AbilityState> entry : abilityStates.entrySet()) {
					Player player = Bukkit.getPlayer(entry.getKey());
					AbilityState value = entry.getValue();

					for (Map.Entry<Integer, Integer> recastEntry : value.getRecasts().entrySet()) {

						int recastIndex = recastEntry.getKey();
						int recastSeconds = recastEntry.getValue();

						Ability ability = championsManager.getSelectedChampion(player).getAbilities()[recastIndex];

						if (recastSeconds == 0) {
							removeRecastCooldown(player, ability, recastIndex);
						} else if (recastSeconds > 0) {
							itemManager.setItem(player, recastIndex,
									new ItemBuilder(ability.getIcon().clone()).setAmount(recastSeconds).toItemStack());
							value.setRecastSeconds(recastIndex, recastSeconds - 1);
						}
					}

					if (value.getCooldowns() != null) {

						int[] array = value.getCooldowns();
						for (int i = 0; i < array.length; i++) {
							Ability ability = championsManager.getSelectedChampion(player).getAbilities()[i];
							if (array[i] == 0) {
								if (player.getInventory().getItem(i).getType() == ability.getIcon().getType() && player
										.getInventory().getItem(i).getAmount() == ability.getIcon().getAmount())
									continue;
								itemManager.setItem(player, i, ability.getIcon());
							} else if (array[i] > 0) {
								itemManager.setItem(player, i, createOnCooldownItem(array[i], ability.getName()));
								array[i]--;
							}
						}

					}

					if (value.getDisabled() != null) {
						if (value.getDisabled() <= 1) {
							value.setDisabled(null);
							ItemStack[] cache = getAbilityState(player).getCachedIcons();
							for (int i = 0; i < cache.length; i++)
								itemManager.setItem(player, i, cache[i]);
							getAbilityState(player).setCachedIcons(null);
						} else {
							int remaining = value.getDisabled() - 1;
							value.setDisabled(remaining);
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

	private AbilityState getAbilityState(Player player) {
		AbilityState abiltyState = abilityStates.getOrDefault(player.getUniqueId(), null);
		if (abiltyState == null) {
			abiltyState = new AbilityState();
			abilityStates.put(player.getUniqueId(), abiltyState);
		}
		return abiltyState;
	}

	public void addCooldown(Player player, int index, int cooldown) {
		if (getAbilityState(player).getCooldowns() != null) {
			int[] array = getAbilityState(player).getCooldowns();
			array[index] = cooldown;
		} else {
			int[] array = new int[] { 0, 0, 0, 0 };
			array[index] = cooldown;
			getAbilityState(player).setCooldowns(array);

		}
	}

	public void addRecastCooldown(Player player, int index, int cooldown) {
		if (getAbilityState(player).getCooldowns() != null)
			getAbilityState(player).setCooldowns(null);

		if (!getAbilityState(player).getRecasts().containsKey(index)) {
			getAbilityState(player).setRecastSeconds(index, cooldown);

		}
	}

	public void removeRecastCooldown(Player player, Ability ability, int index) {
		if (getAbilityState(player).getRecasts().containsKey(index)) {
			getAbilityState(player).removeRecast(index);

			itemManager.setItem(player, index, ability.getIcon());
		}
		if (getAbilityState(player).getCooldowns() == null) {
			int[] array = new int[] { 0, 0, 0, 0 };
			getAbilityState(player).setCooldowns(array);
		}
	}

	public void removeCooldown(Player player) {
		if (getAbilityState(player).getCooldowns() != null)
			getAbilityState(player).setCooldowns(null);
	}

	public void disableAbilities(Player player, int duration) {
		int remaining = duration;
		ItemStack[] cachedItems = new ItemStack[4];
		if (getAbilityState(player).getDisabled() != null) {
			remaining += getAbilityState(player).getDisabled();

			Ability[] abilities = championsManager.getSelectedChampion(player).getAbilities();
			for (int i = 0; i < abilities.length; i++) {
				cachedItems[i] = abilities[i].getIcon();
			}
		} else {
			for (int i = 0; i < 4; i++) {
				cachedItems[i] = player.getInventory().getItem(i);
			}
		}
		getAbilityState(player).setCachedIcons(cachedItems);

		getAbilityState(player).setDisabled(remaining);

		for (int i = 0; i < 4; i++) {
			itemManager.setItem(player, i,
					new ItemBuilder(new ItemStack(Material.BARRIER)).setDisplayName(ChatColor.RED + "X")
							.setLore(ChatColor.GRAY + "This ability is not useable").toItemStack());
		}
	}

}
