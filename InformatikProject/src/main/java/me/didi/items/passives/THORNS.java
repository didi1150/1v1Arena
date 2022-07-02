package me.didi.items.passives;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.items.ItemPassive;
import me.didi.items.ItemPassiveCooldownManager;

public class THORNS implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (ItemPassiveCooldownManager.getInstance().isOnCooldown(player, slot))
			return;

		ItemPassiveCooldownManager.getInstance().addCooldown(this, player, slot, player.getInventory().getItem(slot));
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String[] getDescription() {
		return null;
	}

	@Override
	public int getCooldown() {
		return 0;
	}

}
