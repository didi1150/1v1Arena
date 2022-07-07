package me.didi.items.passives;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.items.ItemPassive;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;

public class FRAY implements ItemPassive {

	private boolean hasRefreshedEffect = false;
	private boolean firstTimeHit = true;

	@Override
	public void runPassive(Event event, Player player, int slot) {

		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;

			if (customDamageEvent.isCancelled())
				return;
			if (customDamageEvent.getAttacker() != player)
				return;

			if (!hasRefreshedEffect && !firstTimeHit)
				hasRefreshedEffect = true;

			if (firstTimeHit) {
				firstTimeHit = false;

				ItemStack item = player.getInventory().getItem(slot).clone();
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.spigot().setUnbreakable(false);
				item.setItemMeta(itemMeta);

				ItemStack barrier = new ItemBuilder(new ItemStack(Material.BARRIER))
						.setDisplayName(ChatColor.RED + "NA").setLore(ChatColor.GRAY + "This slot is not available!")
						.toItemStack();

				float originalSpeed = player.getWalkSpeed();
				float bonusSpeed = 0.05f;
				player.setWalkSpeed(player.getWalkSpeed() + bonusSpeed);

				player.getInventory().setItem(slot - 4, item);

				TaskManager.getInstance().repeatUntil(0, 1, 20 * 2, new BiConsumer<BukkitTask, AtomicLong>() {

					float percentage = 0;
					float maxPercentage = 1;
					float amountOfSeconds = 2;

					@Override
					public void accept(BukkitTask task, AtomicLong counter) {
						if (percentage < maxPercentage)
							percentage += maxPercentage / amountOfSeconds / 20;

						if (hasRefreshedEffect) {
							hasRefreshedEffect = false;
							counter.set(0);
							percentage = 0;
						}

						if (counter.get() >= 20 * 2) {
							player.setWalkSpeed(originalSpeed);
							player.getInventory().setItem(slot - 4, barrier);
							firstTimeHit = true;
						}

						item.setDurability((short) (item.getType().getMaxDurability() * percentage));
						player.getInventory().setItem(slot - 4, item);
					}
				});
			}
		}

	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "FRAY";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": Basic attacks deal " + ChatColor.DARK_AQUA + "42",
				ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "bonus" + ChatColor.RESET + ChatColor.DARK_AQUA
						+ "magic damage" + ChatColor.GOLD + " on-hit",
				ChatColor.GRAY + "and grant you " + ChatColor.YELLOW + "5 " + ChatColor.BOLD + "bonus "
						+ ChatColor.RESET + ChatColor.YELLOW + "movement speed",
				ChatColor.GRAY + "for 2 seconds." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
