package me.didi.items.passives;

import java.awt.Color;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;
import me.didi.player.CurrentStatGetter;
import net.md_5.bungee.api.ChatColor;
import xyz.xenondevs.particle.ParticleEffect;

public class THORNS implements ItemPassive {

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.getEntity() == player) {
				double damage = CurrentStatGetter.getInstance().getCurrentArmor(player) * 0.2 + 10;
				DamageManager.damageEntity(player, customDamageEvent.getAttacker(), DamageReason.MAGIC, damage, false);

				ParticleEffect.REDSTONE.display(customDamageEvent.getAttacker().getLocation().add(0, 1.5, 0),
						Color.CYAN);
			}
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "THORNMAIL";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": When struck by a basic attack " + ChatColor.GOLD
				+ "on-hit" + ChatColor.GRAY + ", deal " + ChatColor.DARK_AQUA + "10 " + ChatColor.YELLOW
				+ "(+ 20% bonus armor) " + ChatColor.DARK_AQUA + "magic damage " + ChatColor.GRAY
				+ "to the attacker." };
	}

	@Override
	public int getCooldown() {
		return 0;
	}

}
