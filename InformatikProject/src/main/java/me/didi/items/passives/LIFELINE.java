package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomPlayerHealthChangeEvent;
import me.didi.items.ItemPassive;
import me.didi.items.ItemPassiveCooldownManager;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.TaskManager;

public class LIFELINE implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot) {

		if (event instanceof CustomPlayerHealthChangeEvent) {
			CustomPlayerHealthChangeEvent customPlayerHealthChangeEvent = (CustomPlayerHealthChangeEvent) event;
			CustomPlayer customPlayer = customPlayerHealthChangeEvent.getCustomPlayer();

			if (customPlayerHealthChangeEvent.getChangedHealth() < CurrentStatGetter.getInstance().getMaxHealth(player)
					* 0.3) {

				if (ItemPassiveCooldownManager.getInstance().isOnCooldown(player, slot))
					return;

				ItemPassiveCooldownManager.getInstance().addCooldown(this, player, slot,
						player.getInventory().getItem(slot));

				float shield = (float) (CustomPlayerManager.getInstance().getBonusHealth(player) * 0.75);
				customPlayer.setRemainingShield(shield);

				TaskManager.getInstance().repeatUntil(1, 1, (long) (3.75 * 20), (task, counter) -> {
					float perTickRemove = (shield / 3.75f) / 20;
					if (customPlayer.getRemainingShield() == 0) {
						task.cancel();
						return;
					}

					if (customPlayer.getRemainingShield() - perTickRemove <= 0)
						customPlayer.setRemainingShield(0);
					else
						customPlayer.setRemainingShield(customPlayer.getRemainingShield() - perTickRemove);

				});
			}
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "Lifeline";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": If you would take damage",
				ChatColor.GRAY + "that would reduce you below " + ChatColor.GREEN + "30% of your",
				ChatColor.GREEN + "maximum health" + ChatColor.GRAY + ", gain a " + ChatColor.GOLD + "shield "
						+ ChatColor.GRAY + "equal to",
				ChatColor.GREEN + "75% of bonus health " + ChatColor.GRAY + "that decays over 3.75",
				ChatColor.GRAY + "seconds (60 second cooldown)." };
	}

	@Override
	public int getCooldown() {
		return 60;
	}

}
