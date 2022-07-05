package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;

public class IGNORE_PAIN implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.getDamageReason() != DamageReason.TRUE) {
				
			}
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "IGNORE PAIN";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": Stores",
				ChatColor.GRAY + "(" + ChatColor.GOLD + "Melee " + ChatColor.GRAY + "30% / " + ChatColor.GOLD
						+ "Ranged " + ChatColor.GRAY + "10%) of all post-mitigation",
				ChatColor.RED + "physical and " + ChatColor.DARK_AQUA + "magic damage received,",
				ChatColor.GRAY + "including on shields, which is successively",
				ChatColor.GRAY + "taken as " + ChatColor.RED + "true damage" + ChatColor.GRAY + " over 3 seconds",
				ChatColor.GRAY + "instead, dealing a third of the stored damage", ChatColor.GRAY + "each second." };
	}

	@Override
	public int getCooldown() {
		return 0;
	}

}
