package me.didi.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.didi.player.effects.SpecialEffect;

public class CustomPlayerManager {

	private Map<UUID, CustomPlayer> players = new HashMap<>();
	private Map<CustomPlayer, SpecialEffect> nextOnHitEffect = new HashMap<>();

	public void addPlayer(Player player) {

		UUID uuid = player.getUniqueId();
		String name = player.getName();

		players.put(uuid, new CustomPlayer(100, 100, 0, 0, 0, uuid, name));

	}

	public void removePlayer(UUID uuid) {
		players.remove(uuid);
	}

	private CustomPlayer getPlayer(UUID uuid) {
		if (players.containsKey(uuid)) {
			for (Map.Entry<UUID, CustomPlayer> entry : players.entrySet()) {
				if (entry.getKey() == uuid) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	public void addSpecialEffect(UUID uuid, SpecialEffect specialEffect) {
		nextOnHitEffect.put(getPlayer(uuid), specialEffect);
	}

	public void removeSpecialEffect(UUID uuid) {
		nextOnHitEffect.remove(getPlayer(uuid));
	}

}
