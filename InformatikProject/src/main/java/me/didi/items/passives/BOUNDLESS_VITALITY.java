package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.items.ItemPassive;

public class BOUNDLESS_VITALITY implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot, int index) {
		if (event instanceof CustomPlayerHealEvent) {
			CustomPlayerHealEvent healEvent = (CustomPlayerHealEvent) event;
			if (healEvent.isCancelled())
				return;

			if (player.getUniqueId() == healEvent.getCustomPlayer().getUuid()) {
				healEvent.setHealAmount(healEvent.getHealAmount() * 1.5f);
			}
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "BOUNDLESS VITALITY";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": Increases all", ChatColor.GOLD + "healing "
				+ ChatColor.GRAY + "and " + ChatColor.GOLD + "shielding" + ChatColor.GRAY + " received by",
				ChatColor.GRAY + "25%." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
