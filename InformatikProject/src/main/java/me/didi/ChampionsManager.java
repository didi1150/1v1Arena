package me.didi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.ability.Ability;
import me.didi.ability.AbilityType;
import me.didi.characters.Champion;
import me.didi.characters.champions.impl.Anakin;
import me.didi.characters.champions.impl.Lloyd;
import me.didi.characters.champions.impl.Perry;
import me.didi.characters.champions.impl.Rex;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;
import net.md_5.bungee.api.ChatColor;

public class ChampionsManager {

	private Set<Champion> selectableChampions = new HashSet<Champion>();
	private Map<UUID, Champion> selectedChampions = new HashMap<UUID, Champion>();

	public void registerChampions() {
		selectableChampions.add(new Lloyd("Lloyd",
				new Ability[] { new Ability(AbilityType.MELEE, "First Ability", new ItemStack(Material.INK_SACK), 10),

						new Ability(AbilityType.MELEE, "First Ability", new ItemStack(Material.INK_SACK), 10),

						new Ability(AbilityType.MELEE, "First Ability", new ItemStack(Material.INK_SACK), 10),

						new Ability(AbilityType.MELEE, "Spinjitzu", new ItemStack(Material.STRING), 10) },
				50, 50, 50, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_LLOYD),
				new ItemBuilder(new ItemStack(Material.GOLD_SWORD)).setDisplayName(ChatColor.GOLD + "Katana")
						.setLore(ChatColor.GRAY + "damage: " + ChatColor.RED + "6").toItemStack()));
		selectableChampions.add(new Anakin("Anakin",
				new Ability[] { new Ability(AbilityType.MELEE, "Enlightenment", new ItemStack(Material.IRON_SWORD), 10),

						new Ability(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

						new Ability(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

						new Ability(AbilityType.MELEE, "CHOKE", new ItemStack(Material.WOOD), 10) },
				75, 50, 50, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_ANAKIN),
				new ItemBuilder(new ItemStack(Material.STICK)).setDisplayName(ChatColor.AQUA + "Lightsaber").addGlow()
						.setLore(ChatColor.GRAY + "damage: " + ChatColor.RED + "7").toItemStack()));
		selectableChampions.add(new Rex("Rex", new Ability[] {

				new Ability(AbilityType.RANGED, "Blast'em",
						new ItemBuilder(new ItemStack(Material.ARROW)).setDisplayName(ChatColor.AQUA + "Blast'em")
								.toItemStack(),
						10),

				new Ability(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

				new Ability(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

				new Ability(AbilityType.MELEE, "Airstrike",
						new ItemBuilder(new ItemStack(Material.BEACON))
								.setDisplayName(org.bukkit.ChatColor.GOLD + "Airstrike").toItemStack(),
						10) },
				75, 50, 50, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_REX),
				new ItemBuilder(new ItemStack(Material.IRON_BARDING)).setDisplayName(ChatColor.AQUA + "Blaster")
						.addGlow().setLore(ChatColor.GRAY + "damage: " + ChatColor.RED + "5").toItemStack()));
		selectableChampions.add(new Perry("Perry", new Ability[] {

				new Ability(AbilityType.OTHER, "Swift Throw",
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
								.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(),
						20),

				new Ability(AbilityType.OTHER, "Get Away",
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
								.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(),
						20),

				new Ability(AbilityType.OTHER, "Fist",
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
								.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(),
						20),

				new Ability(AbilityType.OTHER, "Enchant", new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
						.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(), 20)

		}, 100, 0, 0, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_PERRY),
				new ItemBuilder(new ItemStack(Material.ANVIL)).setDisplayName(ChatColor.GREEN + "Fist").toItemStack()));
	}

	public Set<Champion> getSelectableChampions() {
		return selectableChampions;
	}

	public void setSelectedChampion(UUID uuid, Champion champion) {
		champion.setPlayer(Bukkit.getPlayer(uuid));
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
