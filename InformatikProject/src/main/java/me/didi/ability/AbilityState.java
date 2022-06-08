package me.didi.ability;

import org.bukkit.inventory.ItemStack;

public class AbilityState {

	private int[] cooldowns;
	private Integer recastIndex;
	private Integer recastSeconds;

	private Integer disabled;
	private ItemStack[] cachedIcons;

	public void setCooldowns(int[] cooldowns) {
		this.cooldowns = cooldowns;
	}

	public void setRecastIndex(Integer recastIndex) {
		this.recastIndex = recastIndex;
	}

	public void setRecastSeconds(Integer recastSeconds) {
		this.recastSeconds = recastSeconds;
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

	public Integer getRecastIndex() {
		return recastIndex;
	}

	public Integer getRecastSeconds() {
		return recastSeconds;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public ItemStack[] getCachedIcons() {
		return cachedIcons;
	}

	
	
}
