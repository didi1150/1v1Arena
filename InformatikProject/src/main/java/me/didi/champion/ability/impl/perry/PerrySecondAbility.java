package me.didi.champion.ability.impl.perry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.champion.ability.Recastable;
import me.didi.events.customEvents.AbilityCastEvent;
import me.didi.events.customEvents.CustomPlayerHealEvent;
import me.didi.events.customEvents.HealReason;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;

public class PerrySecondAbility extends Recastable implements Ability {

	private Map<Player, Minecart> cars = new HashMap<Player, Minecart>();
	private Map<Player, BukkitTask> bukkitTasks = new HashMap<Player, BukkitTask>();
	private PerrySecondAbility instance;

	public PerrySecondAbility() {
		instance = this;
	}

	@Override
	public String getName() {
		return ChatColor.AQUA + "Get Away";
	}

	@Override
	public ItemStack getIcon() {
		// TODO Auto-generated method stub
		return new ItemBuilder(new ItemStack(Material.MINECART)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Perry hops into his car, driving around",
				ChatColor.GRAY + "while regenerating " + ChatColor.GREEN + "30% " + ChatColor.GRAY + "of his"
						+ ChatColor.GREEN + " max health",
				ChatColor.GRAY + "over the next 8 seconds.",
				ChatColor.GRAY + "He can choose to recast this ability, to hop out of his car early" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.MOVEMENT;
	}

	@Override
	public int getCooldown() {
		return 10;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		AbilityCastEvent event = new AbilityCastEvent(player, getAbilityType());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		getRecasts()[recastCounters.getOrDefault(player, 0)].accept(player, abilityStateManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BiConsumer<Player, AbilityStateManager>[] getRecasts() {
		BiConsumer<Player, AbilityStateManager>[] consumers = new BiConsumer[2];

		consumers[0] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				hopIntoCar(player, abilityStateManager);
			}

			private void hopIntoCar(Player player, AbilityStateManager abilityStateManager) {
				recastCounters.put(player, 1);
				double healAmount = (CustomPlayerManager.getInstance().getBonusHealth(player)
						+ CustomPlayerManager.getInstance().getPlayer(player.getUniqueId()).getBaseHealth()) * 0.3;

				double healAmountPerTick = healAmount / (20 * (getRecastCountdown() - 2) * 20);

				abilityStateManager.addRecastCooldown(player, 1, getRecastCountdown());
				Minecart minecart = (Minecart) player.getWorld().spawnEntity(player.getLocation(), EntityType.MINECART);
				minecart.setPassenger(player);

				cars.put(player, minecart);

				bukkitTasks.put(player,
						TaskManager.getInstance().repeatUntil(0, 1, 20 * getRecastCountdown(), (task, counter) -> {
							if (counter.get() >= 20 * getRecastCountdown()) {
								hopOut(player, abilityStateManager);
							}

							minecart.setVelocity(player.getLocation().getDirection().multiply(10).setY(0.01));
							minecart.setPassenger(player);

							if (counter.get() < 20 * (getRecastCountdown() - 2) * 20)
								Bukkit.getPluginManager()
										.callEvent(new CustomPlayerHealEvent(
												CustomPlayerManager.getInstance().getPlayer(player.getUniqueId()),
												HealReason.OTHER, (float) healAmountPerTick));
						}));
			}
		};

		consumers[1] = new BiConsumer<Player, AbilityStateManager>() {

			@Override
			public void accept(Player player, AbilityStateManager abilityStateManager) {
				hopOut(player, abilityStateManager);
			}

		};

		return consumers;
	}

	@Override
	public int getRecastCountdown() {
		return 10;
	}

	private void hopOut(Player player, AbilityStateManager abilityStateManager) {
		recastCounters.put(player, 0);
		abilityStateManager.removeRecastCooldown(player, instance, 1);
		abilityStateManager.addCooldown(player, 1, getCooldown());

		bukkitTasks.get(player).cancel();
		bukkitTasks.remove(player);

		Minecart minecart = cars.get(player);
		cars.remove(player);

		minecart.eject();
		minecart.remove();
	}

}
