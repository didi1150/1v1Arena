package me.didi.utilities;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import xyz.xenondevs.particle.ParticleEffect;

public class Lightning {
	public static final float RED_ZERO = 0.005f;
	public static final int NO_LIMIT = -1;

	public static final int RGB_START_DEFAULT = 0x9922FF;
	public static final int RGB_END_DEFAULT = 0x7777FF;

	private static float DIST_BETWEEN = 0.2f;

	private final Location start;
	private final Location end;

	private final @Nonnull List<Lightning> adjacentLightnings = Lists.newArrayList();

	public List<Lightning> getAdjacentLightnings() {
		return adjacentLightnings;
	}

	public Lightning(@Nonnull Location start, @Nonnull Location end) {
		this.start = start;
		this.end = end;
		if (!start.getWorld().equals(end.getWorld())) {
			throw new IllegalArgumentException("Start and end position must be in same world!");
		}
	}

	public int getParticleAmount(float distBetween) {
		int amountParticles = (int) (start.distance(end) / distBetween);
		if (amountParticles == 0) {
			return 0;
		}
		return amountParticles;
	}

	public Set<Entity> create(int rgbStart, int rgbEnd, float distBetween) {
		int amountParticles = getParticleAmount(distBetween);
		ColorTransition transition = new ColorTransition(rgbStart, rgbEnd, amountParticles);
		Vector dir = end.clone().toVector().subtract(start.clone().toVector()).normalize().multiply(distBetween);
		Set<Entity> entities = Sets.newHashSet();
		for (int index = 0; index < amountParticles; index++) {
			Color color = transition.nextColor();
			Location particleLoc = start.clone().add(dir.clone().multiply(index));

			ParticleEffect.REDSTONE.display(particleLoc,
					new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue()));
			entities.addAll(particleLoc.getWorld().getNearbyEntities(particleLoc, 1, 1, 1));

			if (start.getWorld().getBlockAt(particleLoc).getType() != Material.AIR) {
				break;
			}

		}
		return entities;
	}

	/**
	 * Recursively counts the total amount of lightnings chained together. Circular
	 * connections will eventually throw StackOverflowException
	 *
	 * @param lightnings Lightnings to count
	 * @return amount of total lightnings, accounting adjacent lightnings to other
	 *         lightnings
	 */
	public static int countTotalLightnings(@Nonnull Collection<Lightning> lightnings) {
		AtomicInteger amount = new AtomicInteger(lightnings.size());
		lightnings.forEach(lightning -> amount.addAndGet(countTotalLightnings(lightning.adjacentLightnings)));
		return amount.get();
	}

	/**
	 * Will recursively create all lightnings that are found. On limit, drawing will
	 * not be continued.
	 *
	 * @param lightnings Lightnings that should be drawn
	 * @param limit      how many lightnings should be drawn at max
	 */
	public static Set<Entity> create(@Nonnull Collection<Lightning> lightnings, int limit) {
		final ColorTransition transition = new ColorTransition(Lightning.RGB_START_DEFAULT, Lightning.RGB_END_DEFAULT,
				countTotalLightnings(lightnings));
		return create(lightnings, limit, transition);
	}

	public static Set<Entity> create(@Nonnull Collection<Lightning> lightnings, final int limit,
			@Nonnull ColorTransition transition) {
		if (limit == 0) {
			return ImmutableSet.of();
		}
		AtomicReference<ColorTransition> refTrans = new AtomicReference<>(transition);
		AtomicInteger atomicLimit = new AtomicInteger(limit);
		Set<Entity> entities = Sets.newHashSet();
		lightnings.forEach(lightning -> {
			if (atomicLimit.get() == 0 && limit != NO_LIMIT) {
				return;
			}
			int amountParticles = lightning.getParticleAmount(Lightning.DIST_BETWEEN);
			ColorTransition currentTransition = refTrans.get().clone();
			entities.addAll(lightning.create(refTrans.get().nextColor().asRGB(),
					refTrans.get().nextColor(amountParticles).asRGB(), Lightning.DIST_BETWEEN));
			entities.addAll(create(lightning.getAdjacentLightnings(), atomicLimit.decrementAndGet(), refTrans.get()));
			refTrans.set(currentTransition);
		});
		return entities;
	}

	public static Set<Entity> create(@Nonnull Collection<Lightning> lightnings) {
		return create(lightnings, NO_LIMIT);
	}

	public static Set<Entity> create(@Nonnull Lightning lightning) {
		return create(lightning, NO_LIMIT);
	}

	public static Set<Entity> create(@Nonnull Lightning lightning, int limit) {
		return create(ImmutableList.of(lightning), limit);
	}

}
