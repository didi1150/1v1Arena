package me.didi;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.didi.characters.Champion;
import me.didi.characters.champions.impl.Lloyd;
import me.didi.utilities.ItemBuilder;

public class ChampionsManager {

	private Set<Champion> selectableChampions = new HashSet<Champion>();

	private void registerChampions() {
		selectableChampions.add(new Lloyd("Lloyd", 100, 50, 50, new ItemBuilder(new ItemStack(Material.SKULL_ITEM)).setDisplayName("Lloyd").toItemStack())));
	}

	public ChampionsManager() {
		registerChampions();
	}

}
