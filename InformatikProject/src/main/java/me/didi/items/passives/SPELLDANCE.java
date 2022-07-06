package me.didi.items.passives;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.items.ItemPassive;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.ParticleUtils;
import me.didi.utilities.TaskManager;
import xyz.xenondevs.particle.ParticleEffect;

public class SPELLDANCE implements ItemPassive {

	private int counter = 0;
	private boolean isWithinThreeSeconds = true;
	private boolean hasRefreshedEffect = false;
	private boolean isActive = false;

	@Override
	public void runPassive(Event event, Player player, int slot) {

		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.isCancelled())
				return;
			if (customDamageEvent.getAttacker() != player)
				return;

			if (counter == 0 && isWithinThreeSeconds) {
				isWithinThreeSeconds = false;
				ChatUtils.sendDebugMessage("Pre Effect started");

				TaskManager.getInstance().runTaskLater(20 * 3, task -> {
					ChatUtils.sendDebugMessage("Reset counter");
					isWithinThreeSeconds = true;
					counter = 0;
				});
			}

			if (counter >= 3) {
				counter = 0;

				if (!isActive) {
					isActive = true;

					ItemStack item = player.getInventory().getItem(slot).clone();
					ItemStack barrier = new ItemBuilder(new ItemStack(Material.BARRIER))
							.setDisplayName(ChatColor.RED + "NA")
							.setLore(ChatColor.GRAY + "This slot is not available!").toItemStack();

					float originalSpeed = player.getWalkSpeed();
					float bonusSpeed = 1.15f;
					player.setWalkSpeed(player.getWalkSpeed() * bonusSpeed);

					ParticleUtils.createSphere(ParticleEffect.REDSTONE, Color.WHITE, player.getLocation().add(0, 1, 0),
							1.5);

					player.getInventory().setItem(slot - 4, item);

					TaskManager.getInstance().repeatUntil(20, 1, 20 * 5, new BiConsumer<BukkitTask, AtomicLong>() {
						float bSpeed = 1.15f;
						float percentage = 0;
						float maxValue = 1;
						float amountOfSeconds = 5;

						@Override
						public void accept(BukkitTask task, AtomicLong counter) {
							if (hasRefreshedEffect) {
								hasRefreshedEffect = false;
								counter.set(0);
								percentage = 0;
							}

							if (counter.get() % 5 == 0 && counter.get() <= 20 * 2 && bonusSpeed > 1.02f) {
								bSpeed -= 0.13f / 8;
								percentage += maxValue / amountOfSeconds / 4;
								item.setDurability((short) (item.getType().getMaxDurability() * percentage));
								player.getInventory().setItem(slot - 4, item);
								player.setWalkSpeed(bSpeed);
							}

							if (counter.get() >= 20 * 5) {
								isActive = false;
								player.getInventory().setItem(slot - 4, barrier);
								player.setWalkSpeed(originalSpeed);
								task.cancel();
								return;
							}

						}
					});
				}
			}

			counter++;
			ChatUtils.sendDebugMessage("Counter: " + counter + " [" + this.toString() + "]");
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "SPELLDANCE";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": After dealing 3",
				ChatColor.GRAY + "instances of damage from basic attacks or",
				ChatColor.GOLD + "ability damage " + ChatColor.GRAY + "to champions within 3",
				ChatColor.GRAY + "seconds, gain " + ChatColor.YELLOW
						+ "15% bonus movement speed, decaying to 2% over 2 seconds, and " + ChatColor.DARK_PURPLE
						+ "40",
				ChatColor.DARK_PURPLE + "ability power " + ChatColor.GRAY + "for 5 seconds, refreshing on",
				ChatColor.GRAY + "damage dealt to champions though not", ChatColor.GRAY + "resetting the effect." };
	}

	@Override
	public int getCooldown() {
		return 5;
	}

}
