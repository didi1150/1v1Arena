package me.didi.items.passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;

public class DEATH implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot, int index) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.getAttacker().getUniqueId() == player.getUniqueId()) {
				Player victim = (Player) customDamageEvent.getEntity();
				CustomPlayer victimPlayer = CustomPlayerManager.getInstance().getPlayer(victim);
				if (victimPlayer.getCurrentHealth() - customDamageEvent.getDamage() <= CurrentStatGetter.getInstance()
						.getMaxHealth(victim) * 0.05) {
					DamageManager.damageEntity(player, victim, DamageReason.TRUE,
							CurrentStatGetter.getInstance().getMaxHealth(victim), false);
				}
			}
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "DEATH";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": If you deal post-",
				ChatColor.GRAY + "mitigation damage that would leave a",
				ChatColor.GRAY + "champion below " + ChatColor.GREEN + "5% of their " + ChatColor.BOLD + "maximum",
				ChatColor.GREEN + "health, " + ChatColor.GOLD + "execute them." };
	}

	@Override
	public int getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

}
