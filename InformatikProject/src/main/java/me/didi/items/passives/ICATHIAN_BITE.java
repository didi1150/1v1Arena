
package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;
import me.didi.player.CurrentStatGetter;

public class ICATHIAN_BITE implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot, int index) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;

			if (customDamageEvent.getDamageReason() != DamageReason.AUTO)
				return;

			if (customDamageEvent.getAttacker().getUniqueId() == player.getUniqueId()) {

				double extraDamage = 15 + CurrentStatGetter.getInstance().getAbilityPower(player) * 0.2;

				DamageManager.damageEntity(player, customDamageEvent.getEntity(), DamageReason.MAGIC, extraDamage,
						false);

			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return ChatColor.GOLD + "" + ChatColor.BOLD + "ICATHIAN BITE";
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { getName() + ChatColor.GRAY + ": Basic attacks deal",
				ChatColor.DARK_AQUA + "15 " + ChatColor.DARK_PURPLE + "(+ 20%)" + ChatColor.DARK_AQUA + ""
						+ ChatColor.BOLD + " bonus" + ChatColor.RESET + ChatColor.DARK_AQUA + " magic damage "
						+ ChatColor.GOLD + "on-hit" + ChatColor.GRAY + "." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
