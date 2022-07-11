package me.didi.items.passives;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
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
import me.didi.events.customEvents.DamageManager;
import me.didi.events.customEvents.DamageReason;
import me.didi.items.ItemPassive;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import me.didi.utilities.Utils;

class DamageStack {
	private double damage;

	public DamageStack(double damage) {
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}
}

public class IGNORE_PAIN implements ItemPassive {

	private AtomicLong sharedCounter = new AtomicLong(0);
	private AtomicInteger amount = new AtomicInteger();
	private BukkitTask bukkitTask;
	private Player player;
	private Player attackerPlayer;
	private int slot;

	private Queue<DamageStack> stackQueue = new LinkedList<DamageStack>();

	public IGNORE_PAIN() {

		TaskManager.getInstance().repeat(0, 1, task -> {
			if (!stackQueue.isEmpty() && player != null) {
				int amount = stackQueue.size();

				if (bukkitTask == null) {
					DamageStack damageStack = stackQueue.poll();
					double storedDamage = damageStack.getDamage();

					ItemStack item = player.getInventory().getItem(slot).clone();
					ItemStack barrier = new ItemBuilder(new ItemStack(Material.BARRIER))
							.setDisplayName(ChatColor.RED + "NA")
							.setLore(ChatColor.GRAY + "This slot is not available!").toItemStack();

					TaskManager.getInstance().repeatUntil(1, 20, 3, (task2, counter) -> {
						DamageManager.damageEntity(attackerPlayer, player, DamageReason.TRUE, storedDamage / 3, false);
						if (counter.get() >= 3) {
							task2.cancel();
						}
					});

					bukkitTask = Utils.showEffectStatus(player, slot, 3, 20, item, barrier, sharedCounter, () -> {
						bukkitTask = null;
					});
				}
				if (player.getInventory().getItem(slot - 4).getType() != Material.BARRIER && amount >= 1) {
					ItemStack cloned = player.getInventory().getItem(slot - 4);
					cloned.setAmount(cloned.getAmount() + 1);
					player.getInventory().setItem(slot - 4, cloned);
				}
			}

		});

	}

	@Override
	public void runPassive(Event event, Player player, int slot) {
		this.slot = slot;
		if (event instanceof CustomDamageEvent) {
			CustomDamageEvent customDamageEvent = (CustomDamageEvent) event;
			if (customDamageEvent.isCancelled())
				return;

			if (customDamageEvent.getDamageReason() != DamageReason.TRUE) {
				if (customDamageEvent.getAttacker() == player)
					return;

				this.player = player;

				this.attackerPlayer = (Player) customDamageEvent.getAttacker();
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

				stackQueue.add(new DamageStack(storedDamage));

				if (amount.get() < 64)
					amount.addAndGet(1);
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
