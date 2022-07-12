package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;
import me.didi.utilities.TaskManager;

public class BITTER_COLD implements ItemPassive {

	private float originalSpeed = 0.2f;
	private BukkitTask bukkitTask;

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;

			if (customDamageEvent.getAttacker().getUniqueId() == player.getUniqueId()) {
				if (customDamageEvent.getDamageReason() == DamageReason.AUTO)
					return;

				Player entity = (Player) customDamageEvent.getEntity();

				if (bukkitTask == null) {
					originalSpeed = entity.getWalkSpeed();

					entity.setWalkSpeed(originalSpeed * 0.7f);

					bukkitTask = TaskManager.getInstance().runTaskLater(20 * 1, task -> {
						entity.setWalkSpeed(originalSpeed);
						task.cancel();
						bukkitTask = null;
					});
				} else {
					bukkitTask.cancel();
					bukkitTask = TaskManager.getInstance().runTaskLater(20 * 1, task -> {
						task.cancel();
						entity.setWalkSpeed(originalSpeed);
						bukkitTask = null;
					});
				}
			}
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "BITTER COLD";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": Dealing", ChatColor.GOLD + "ability damage slows",
				ChatColor.GRAY + "affected " + ChatColor.GOLD + "units" + ChatColor.GRAY + " by",
				ChatColor.GRAY + "30% for 1 second." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
