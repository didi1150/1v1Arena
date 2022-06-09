package me.didi.champion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.ability.Ability;
import me.didi.ability.AbilityImpl;
import me.didi.ability.AbilityStateManager;
import me.didi.ability.AbilityType;
import me.didi.ability.impl.LloydUltimate;
import me.didi.champion.characters.impl.Anakin;
import me.didi.champion.characters.impl.Lloyd;
import me.didi.champion.characters.impl.Perry;
import me.didi.champion.characters.impl.Rex;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;
import net.md_5.bungee.api.ChatColor;

public class ChampionsManager {

	private Set<Champion> selectableChampions = new HashSet<Champion>();
	private Map<UUID, Champion> selectedChampions = new HashMap<UUID, Champion>();

	private MainClass plugin;

	public ChampionsManager(MainClass plugin) {
		this.plugin = plugin;
	}

	public void registerChampions(AbilityStateManager abilityCooldownManager,
			SpecialEffectsManager specialEffectsManager, CustomPlayerManager customPlayerManager) {
		selectableChampions.add(new Lloyd("Lloyd",
				new Ability[] {
						new AbilityImpl(AbilityType.MELEE, "First Ability", new ItemStack(Material.INK_SACK), 10),

						new AbilityImpl(AbilityType.MELEE, "First Ability", new ItemStack(Material.INK_SACK), 10),

						new AbilityImpl(AbilityType.MELEE, "First Ability", new ItemStack(Material.INK_SACK), 10),

						new LloydUltimate() },
				50, 50, 50, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_LLOYD),
				new ItemBuilder(new ItemStack(Material.GOLD_SWORD)).setDisplayName(ChatColor.GOLD + "Katana")
						.setLore(ChatColor.GRAY + "damage: " + ChatColor.RED + "6").toItemStack()));
		selectableChampions.add(new Anakin("Anakin",
				new AbilityImpl[] {
						new AbilityImpl(AbilityType.MELEE, "Enlightenment", new ItemStack(Material.IRON_SWORD), 10),

						new AbilityImpl(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

						new AbilityImpl(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

						new AbilityImpl(AbilityType.MELEE, "CHOKE", new ItemStack(Material.WOOD), 10) },
				75, 50, 50, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_ANAKIN),
				new ItemBuilder(new ItemStack(Material.STICK)).setDisplayName(ChatColor.AQUA + "Lightsaber").addGlow()
						.setLore(ChatColor.GRAY + "damage: " + ChatColor.RED + "7").toItemStack()));
		selectableChampions.add(new Rex("Rex", new AbilityImpl[] {

				new AbilityImpl(AbilityType.RANGED, "Blast'em",
						new ItemBuilder(new ItemStack(Material.ARROW)).setDisplayName(ChatColor.AQUA + "Blast'em")
								.toItemStack(),
						10),

				new AbilityImpl(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

				new AbilityImpl(AbilityType.MELEE, "Force", new ItemStack(Material.WOOD), 10),

				new AbilityImpl(AbilityType.MELEE, "Airstrike",
						new ItemBuilder(new ItemStack(Material.BEACON))
								.setDisplayName(org.bukkit.ChatColor.GOLD + "Airstrike").toItemStack(),
						10) },
				75, 50, 50, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_REX),
				new ItemBuilder(new ItemStack(Material.IRON_BARDING)).setDisplayName(ChatColor.AQUA + "Blaster")
						.addGlow().setLore(ChatColor.GRAY + "damage: " + ChatColor.RED + "5").toItemStack()));
		selectableChampions.add(new Perry("Perry", new AbilityImpl[] {

				new AbilityImpl(AbilityType.OTHER, "Swift Throw",
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
								.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(),
						20),

				new AbilityImpl(AbilityType.OTHER, "Get Away",
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
								.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(),
						20),

				new AbilityImpl(AbilityType.OTHER, "Fist",
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
								.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(),
						20),

				new AbilityImpl(AbilityType.OTHER, "Enchant", new ItemBuilder(new ItemStack(Material.LEATHER_HELMET))
						.setDisplayName(ChatColor.GOLD + "Boomerang").toItemStack(), 20)

		}, 100, 0, 0, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_PERRY),
				new ItemBuilder(new ItemStack(Material.ANVIL)).setDisplayName(ChatColor.GREEN + "Fist").toItemStack()));

		selectableChampions.forEach(champion -> {

			champion.setPlugin(plugin);
			champion.setAbilityCooldownManager(abilityCooldownManager);
			champion.setSpecialEffectsManager(specialEffectsManager);
			champion.setCustomPlayerManager(customPlayerManager);
		});
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
