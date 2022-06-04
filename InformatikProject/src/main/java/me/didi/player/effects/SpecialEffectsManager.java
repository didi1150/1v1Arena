package me.didi.player.effects;

import org.bukkit.Bukkit;

import me.didi.events.damageSystem.CustomEffectEvent;

public class SpecialEffectsManager {

	public void effectPlayer(SpecialEffect specialEffect) {
		Bukkit.getPluginManager().callEvent(new CustomEffectEvent(specialEffect));
	}

}
