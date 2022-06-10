package me.didi.champion.ability.impl.lloyd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.AbilityType;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.TaskManager;
import me.didi.utilities.VectorUtils;
import xyz.xenondevs.particle.ParticleEffect;

public class LloydSecondAbility implements Ability {

	static double xArray[] = new double[640];
	static double zArray[] = new double[640];
	static int index = 0;

	static float radius = 1;

	static {

		for (radius = 1; radius < 5; radius += 0.5) {
			for (double t = 0; t < 2 * Math.PI; t += (2 * Math.PI / 80)) {
				float x = radius * (float) Math.sin(t);
				float z = radius * (float) Math.cos(t);
				xArray[index] = x;
				zArray[index] = z;

				index++;
			}

		}
	}

	public static void main(String[] args) {
		System.out.println(radius);
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Smokey Disguise";
	}

	@Override
	public ItemStack getIcon() {
		return new ItemBuilder(new ItemStack(Material.CLAY_BALL)).setDisplayName(getName()).setLore(getDescription())
				.toItemStack();
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return new String[] { ChatColor.GRAY + "Lloyd creates a smoke around himself",
				ChatColor.GRAY + "turning himself invisible" };
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.OTHER;
	}

	@Override
	public int getCooldown() {
		return 15;
	}

	@Override
	public void execute(AbilityStateManager abilityStateManager, Player player,
			SpecialEffectsManager specialEffectsManager) {
		abilityStateManager.addCooldown(player, 1, getCooldown());
		createSmoke(player);
	}

	private void createSmoke(Player player) {
		Location location = VectorUtils.getHighestLocation(player).add(0, 1, 0);
		TaskManager.getInstance().repeatUntil(0, 1, 20 * 15, (task, counter) -> {

			if (counter.get() >= 20 * 15) {
				Bukkit.getOnlinePlayers().forEach(pl -> {
					if (!pl.canSee(player))
						pl.showPlayer(player);
				});
				return;
			}

			if (player.getLocation().distanceSquared(location) <= 25) {
				Bukkit.getOnlinePlayers().forEach(pl -> {
					if (pl.canSee(player))
						pl.hidePlayer(player);
				});
			} else {
				Bukkit.getOnlinePlayers().forEach(pl -> {
					if (!pl.canSee(player))
						pl.showPlayer(player);
				});
			}

			for (double y = 0; y <= 2; y += 0.75) {
				for (int i = 0; i < xArray.length; i++) {
					double x = xArray[i];
					double z = zArray[i];

					Location spawnLocation = location.clone().add(x, y, z);
					ParticleEffect.SMOKE_LARGE.display(spawnLocation);
				}
			}
		});
	}
}
