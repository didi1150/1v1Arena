package me.didi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.characters.Champion;
import me.didi.characters.champions.impl.Anakin;
import me.didi.characters.champions.impl.Lloyd;
import me.didi.utilities.ItemBuilder;

public class ChampionsManager {

	private Set<Champion> selectableChampions = new HashSet<Champion>();
	private Map<UUID, Champion> selectedChampions = new HashMap<UUID, Champion>();

	private void registerChampions() {
		selectableChampions.add(new Lloyd("Lloyd",
				new Ability[] { new Ability("First Ability", new ItemStack(Material.INK_SACK)) }, 50, 50, 50,
				new ItemBuilder(new ItemStack(Material.SKULL_ITEM)).setDisplayName("Lloyd").toItemStack()));
		selectableChampions.add(new Anakin("Anakin",
				new Ability[] { new Ability("Enlightenment", new ItemStack(Material.IRON_SWORD)) }, 75, 50, 50,
				new ItemBuilder(new ItemStack(Material.SKULL_ITEM)).setDisplayName("Anakin").toItemStack()));
	}

	public ChampionsManager() {
		registerChampions();
	}

	public Set<Champion> getSelectableChampions() {
		return selectableChampions;
	}

	public void setSelectedChampion(UUID uuid, Champion champion) {
		selectedChampions.put(uuid, champion);
	}

	public Champion getSelectedChampion(Player player) {
		return selectedChampions.get(player.getUniqueId());
	}

	public Champion getByName(String name) {
		for (Champion champion : selectableChampions) {
			if (champion.getName().equalsIgnoreCase(name)) {
				return champion;
			}
		}
		return null;
	}

}
