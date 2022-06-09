package me.didi.ability;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class AbilityState {

	private int[] cooldowns;
	private Map<Integer, Integer> recasts = new HashMap<Integer, Integer>();

	private Integer disabled;
	private ItemStack[] cachedIcons;

	public void setCooldowns(int[] cooldowns) {
		this.cooldowns = cooldowns;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public void setCachedIcons(ItemStack[] cachedIcons) {
		this.cachedIcons = cachedIcons;
	}

	public int[] getCooldowns() {
		return cooldowns;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public ItemStack[] getCachedIcons() {
		return cachedIcons;
	}

	public Map<Integer, Integer> getRecasts() {
		return recasts;
	}

	public void setRecastSeconds(int index, int seconds) {
		recasts.put(index, seconds);
	}

	public void removeRecast(int index) {
		recasts.remove(index);
	}

}
