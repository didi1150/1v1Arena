package me.didi.champion.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.entity.Player;

public abstract class Recastable {

	protected Map<Player, Integer> recastCounters = new HashMap<>();

	public abstract BiConsumer<Player, AbilityStateManager>[] getRecasts();

	public abstract int getRecastCountdown();

}
