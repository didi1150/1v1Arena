package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.items.ItemPassive;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;

public class GIANT_SLAYER implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.getAttacker() == player) {
				int enemyMaxHealth = CurrentStatGetter.getInstance()
						.getMaxHealth((Player) customDamageEvent.getEntity());
				int attackerMaxHealth = CurrentStatGetter.getInstance().getMaxHealth(player);

				int difference = enemyMaxHealth - attackerMaxHealth;
				if (difference > 0) {
					int steps = difference % 100;
					double value = steps * 0.0075;
					customDamageEvent.setDamage(customDamageEvent.getDamage() * (1 + value));
				} else
					return;
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.GOLD + "" + ChatColor.BOLD + "GIANT SLAYER";
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { getName() + ChatColor.GRAY + ": Deal " + ChatColor.GREEN + "0% - 15%",
				ChatColor.GREEN + "(based on " + ChatColor.BOLD + "maximum" + ChatColor.RESET + ChatColor.GREEN
						+ " health difference)",
				ChatColor.RED + "" + ChatColor.BOLD + "bonus " + ChatColor.RESET + ChatColor.RED + "physical damage "
						+ ChatColor.GRAY + "against enemies",
				ChatColor.GRAY + "champions with greater " + ChatColor.GREEN + "" + ChatColor.BOLD + "maximum "
						+ ChatColor.RESET + ChatColor.GREEN + "health",
				ChatColor.GRAY + "than you." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
