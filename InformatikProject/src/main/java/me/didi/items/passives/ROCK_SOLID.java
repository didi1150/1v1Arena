package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.items.ItemPassive;
import me.didi.player.CurrentStatGetter;
import me.didi.utilities.ChatUtils;

public class ROCK_SOLID implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot, int index) {
		System.out.println("Test");
		if (event instanceof CustomDamageEvent) {
			System.out.println("Event");
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.getEntity() == player) {
				System.out.println("Is player");
				double damage = customDamageEvent.getDamage();
				double reducePercentage = 5.0;
				if (CurrentStatGetter.getInstance().getMaxHealth(player) >= 1000) {
					double toAdd = CurrentStatGetter.getInstance().getMaxHealth(player) % 1000 * 3.5;
					reducePercentage += toAdd;
				}

				if (reducePercentage >= 40.0)
					reducePercentage = 40.0;

				ChatUtils.sendDebugMessage("reducePercentage: " + reducePercentage);

				reducePercentage = 1.00 - (reducePercentage / 100);
				ChatUtils.sendDebugMessage("Damage before: " + damage);
				damage = damage * reducePercentage;
				ChatUtils.sendDebugMessage("Damage Reduced: " + damage);
				customDamageEvent.setDamage(damage);
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.GOLD + "" + ChatColor.BOLD + "ROCK SOLID";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": Every incoming",
				ChatColor.GRAY + "instance of post-mitigation",
				ChatColor.GOLD + "basic damage" + ChatColor.GRAY + " is " + ChatColor.GOLD + "reduced" + ChatColor.GRAY
						+ " by 5 " + ChatColor.GREEN + "(+ 3.5 per",
				ChatColor.GREEN + "1000 " + ChatColor.BOLD + "maximum" + ChatColor.RESET + ChatColor.GREEN
						+ " health), " + ChatColor.GRAY + "" + ChatColor.BOLD + "maximum " + ChatColor.RESET
						+ ChatColor.GRAY + "40%",
				ChatColor.GRAY + "reduction each." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
