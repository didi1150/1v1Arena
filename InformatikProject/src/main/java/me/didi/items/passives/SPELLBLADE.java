package me.didi.items.passives;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.events.customEvents.HealReason;
import me.didi.items.ItemPassive;
import me.didi.items.ItemPassiveCooldownManager;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import net.md_5.bungee.api.ChatColor;

public class SPELLBLADE implements ItemPassive {

	private boolean isActive = false;

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (event instanceof AbilityCastEvent) {
			AbilityCastEvent abilityCastEvent = (AbilityCastEvent) event;
			if (abilityCastEvent.isCancelled())
				return;
			if (abilityCastEvent.getFrom() != player)
				return;

			if (isActive)
				return;

			isActive = true;
			ItemStack cache = new ItemBuilder(new ItemStack(Material.BARRIER)).setDisplayName(ChatColor.RED + "NA")
					.setLore(ChatColor.GRAY + "This slot is not available!").toItemStack();

			ItemStack item = player.getInventory().getItem(slot).clone();
			item.setAmount(10);

			player.getInventory().setItem(slot - 4, item);

			TaskManager.getInstance().repeatUntil(20, 20, 20 * 10, (task, counter) -> {
				int amount = player.getInventory().getItem(slot - 4).getAmount();
				if (amount > 1) {
					player.getInventory().getItem(slot - 4).setAmount(amount - 1);
				} else {
					isActive = false;
					player.getInventory().setItem(slot - 4, cache);
					task.cancel();
				}

			});

		}

		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.isCancelled())
				return;
			if (customDamageEvent.getAttacker() != player)
				return;
			if (customDamageEvent.getDamageReason() != DamageReason.AUTO)
				return;

			if (!isActive)
				return;

			isActive = false;
			ItemPassiveCooldownManager.getInstance().addCooldown(this, player, slot,
					player.getInventory().getItem(slot));

			int baseDamage = CustomPlayerManager.getInstance().getPlayer(player).getAttackDamage();
			double damage = baseDamage + CurrentStatGetter.getInstance().getAttackDamage(player) * 0.4;

			CustomPlayerHealEvent customPlayerHealEvent = new CustomPlayerHealEvent(
					CustomPlayerManager.getInstance().getPlayer(player), HealReason.LIFESTEAL, (float) (baseDamage * 0.4
							+ (CurrentStatGetter.getInstance().getAttackDamage(player) - baseDamage) * 0.16));

			Bukkit.getPluginManager().callEvent(customPlayerHealEvent);

			DamageManager.damageEntity(player, customDamageEvent.getEntity(), DamageReason.PHYSICAL, damage, false);
		}
	}

	@Override
	public String getName() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "SPELLBLADE";
	}

	@Override
	public String[] getDescription() {
		return new String[] { getName() + ChatColor.GRAY + ": After using an ability,",
				ChatColor.GRAY + "your next basic attack within 10 seconds", ChatColor.GRAY + "deals " + ChatColor.RED
						+ "100%" + ChatColor.BOLD + " base " + ChatColor.RESET + ChatColor.RED + "AD (+ 40% "
						+ ChatColor.BOLD + "bonus" + ChatColor.RESET + ChatColor.RED + " AD)",
				ChatColor.GRAY + "bonus physical damage on-hit and restores",
				ChatColor.GREEN + "health " + ChatColor.GRAY + "equal to " + ChatColor.RED + "40% " + ChatColor.BOLD
						+ "base " + ChatColor.RESET + ChatColor.RED + "AD (+ 16% " + ChatColor.BOLD + "bonus",
				ChatColor.GOLD + "AD)" + ChatColor.GRAY + " (1.5 (begins after using the empowered",
				ChatColor.GRAY + "attack) second cooldown)." };
	}

	@Override
	public int getCooldown() {
		return 2;
	}

}
