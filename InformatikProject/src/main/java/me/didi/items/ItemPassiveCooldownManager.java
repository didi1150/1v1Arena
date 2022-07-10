package me.didi.items;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ItemSetter;
import me.didi.utilities.TaskManager;
import net.md_5.bungee.api.ChatColor;

public class ItemPassiveCooldownManager {

	private static ItemPassiveCooldownManager instance;
	private BukkitTask bukkitTask;
	private Set<PassiveCooldown> cooldowns = new HashSet<PassiveCooldown>();
	// infos: slot, cooldown, player

	public void addCooldown(ItemPassive itemPassive, Player player, int slot, ItemStack itemStack) {
		PassiveCooldown passiveCooldown = new PassiveCooldown(player, slot, itemPassive.getCooldown(), itemStack);
		cooldowns.add(passiveCooldown);
	}

	public static ItemPassiveCooldownManager getInstance() {
		if (instance == null)
			instance = new ItemPassiveCooldownManager();
		return instance;
	}

	public void startCounter() {
		bukkitTask = TaskManager.getInstance().repeat(20, 20, task -> {
			Iterator<PassiveCooldown> it = cooldowns.iterator();
			while (it.hasNext()) {
				PassiveCooldown passiveCooldown = it.next();
				Player player = passiveCooldown.getPlayer();
				int slot = passiveCooldown.getSlot();
				ItemStack cachedItem = passiveCooldown.getCachedItem();
				int remainingCooldown = passiveCooldown.getRemainingCooldown();

				if (remainingCooldown == 0) {
					cooldowns.remove(passiveCooldown);
					player.getInventory().setItem(slot, cachedItem);
				} else {
					passiveCooldown.setRemainingCooldown(remainingCooldown - 1);
					String displayName = cachedItem.getItemMeta().getDisplayName();
					new ItemSetter().setItem(player, slot, createOnCooldownItem(remainingCooldown, displayName));
				}
			}
		});
	}

	private ItemStack createOnCooldownItem(int cooldownLeft, String itemName) {
		return new ItemBuilder(new ItemStack(Material.INK_SACK, cooldownLeft, (short) 1)).setDisplayName(itemName)
				.setLore(ChatColor.GRAY + "This Item is on cooldown!").toItemStack();
	}

	public void stopCounter() {
		bukkitTask.cancel();
	}

	public boolean isOnCooldown(Player player, int slot) {
		for (PassiveCooldown passiveCooldown : cooldowns) {
			if (passiveCooldown.getSlot() == slot) {
				return true;
			}
		}
		return false;
	}

}
