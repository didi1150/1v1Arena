package me.didi.items.passives;

import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.Champion;
import me.didi.champion.ChampionsManager;
import me.didi.champion.characters.MeleeChampion;
import me.didi.champion.characters.RangedChampion;
import me.didi.events.customEvents.CustomDamageEvent;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.Utils;

public class IGNORE_PAIN implements ItemPassive {

	private AtomicLong sharedCounter = new AtomicLong(0);
	private int amount = 0;
	private BukkitTask bukkitTask;

	@Override
	public void runPassive(Event event, Player player, int slot) {
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.getDamageReason() != DamageReason.TRUE) {
				if (customDamageEvent.getAttacker() == player)
					return;

				Player attackerPlayer = (Player) customDamageEvent.getAttacker();
				Champion champion = ChampionsManager.getInstance().getSelectedChampion(attackerPlayer);

				double percentage = 0;

				if (champion instanceof RangedChampion) {
					percentage = 0.1;
				}

				if (champion instanceof MeleeChampion) {
					percentage = 0.3;
				}

				double storedDamage = customDamageEvent.getDamage() * percentage;
				customDamageEvent.setDamage(customDamageEvent.getDamage() - storedDamage);

				ItemStack item = player.getInventory().getItem(slot);
				ItemStack barrier = new ItemBuilder(new ItemStack(Material.BARRIER))
						.setDisplayName(ChatColor.RED + "NA").setLore(ChatColor.GRAY + "This slot is not available!")
						.toItemStack();

//				TaskManager.getInstance().repeatUntil(1, 20, 3, (task, counter) -> {
//					DamageManager.damageEntity(attackerPlayer, player, DamageReason.TRUE, storedDamage / 3, false);
//				});

				if (bukkitTask == null)
					bukkitTask = Utils.showEffectStatus(player, slot - 4, 3, 1, item, barrier, sharedCounter);
				else 
					sharedCounter.set(0);
				amount++;
				if (player.getInventory().getItem(slot - 4).getType() != Material.BARRIER) {
					ItemStack cloned = player.getInventory().getItem(slot - 4);
					cloned.setAmount(cloned.getAmount() + 1);
					player.getInventory().setItem(slot - 4, cloned);
				}

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
