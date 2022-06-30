package me.didi.champion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.champion.ability.Ability;
import me.didi.champion.ability.AbilityStateManager;
import me.didi.champion.ability.impl.anakin.AnakinFirstAbility;
import me.didi.champion.ability.impl.anakin.AnakinSecondAbility;
import me.didi.champion.ability.impl.anakin.AnakinThirdAbility;
import me.didi.champion.ability.impl.anakin.AnakinUltimate;
import me.didi.champion.ability.impl.brand.BrandFirstAbility;
import me.didi.champion.ability.impl.brand.BrandSecondAbility;
import me.didi.champion.ability.impl.brand.BrandThirdAbility;
import me.didi.champion.ability.impl.brand.BrandUltimate;
import me.didi.champion.ability.impl.lloyd.LloydFirstAbility;
import me.didi.champion.ability.impl.lloyd.LloydSecondAbility;
import me.didi.champion.ability.impl.lloyd.LloydThirdAbility;
import me.didi.champion.ability.impl.lloyd.LloydUltimate;
import me.didi.champion.ability.impl.perry.PerryFirstAbility;
import me.didi.champion.ability.impl.perry.PerrySecondAbility;
import me.didi.champion.ability.impl.perry.PerryThirdAbility;
import me.didi.champion.ability.impl.perry.PerryUltimate;
import me.didi.champion.ability.impl.rex.RexFirstAbility;
import me.didi.champion.ability.impl.rex.RexSecondAbility;
import me.didi.champion.ability.impl.rex.RexThirdAbility;
import me.didi.champion.ability.impl.rex.RexUltimate;
import me.didi.champion.characters.impl.Anakin;
import me.didi.champion.characters.impl.Brand;
import me.didi.champion.characters.impl.Lloyd;
import me.didi.champion.characters.impl.Perry;
import me.didi.champion.characters.impl.Rex;
import me.didi.player.CustomPlayerManager;
import me.didi.player.effects.SpecialEffectsManager;
import me.didi.utilities.ItemBuilder;
import me.didi.utilities.SkullFactory;
import net.md_5.bungee.api.ChatColor;

public class ChampionsManager {

	private static List<Champion> selectableChampions = new ArrayList<Champion>();
	private static Map<UUID, Champion> selectedChampions = new HashMap<UUID, Champion>();

	private static ChampionsManager instance;

	private ChampionsManager() {
	}

	public static void registerChampions(AbilityStateManager abilityCooldownManager,
			SpecialEffectsManager specialEffectsManager, CustomPlayerManager customPlayerManager, MainClass plugin) {
		selectableChampions.add(new Lloyd(ChatColor.GREEN + "Lloyd", new Ability[] { new LloydFirstAbility(),

				new LloydSecondAbility(),

				new LloydThirdAbility(),

				new LloydUltimate() }, 1040, 41, 45, 75, 55, 0, 0, 0.625f,
				ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_LLOYD),
				new ItemBuilder(Material.GOLD_SWORD).setDisplayName(ChatColor.GOLD + "Katana")
						.setLore(ChatColor.GRAY + "attack damage: " + ChatColor.GOLD + "+6").toItemStack()));
		selectableChampions.add(new Anakin(ChatColor.BLUE + "Anakin", new Ability[] { new AnakinFirstAbility(),

				new AnakinSecondAbility(),

				new AnakinThirdAbility(),

				new AnakinUltimate() }, 1076, 52, 40, 84, 0, 0, 0, 0.638f,
				ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_ANAKIN),
				new ItemBuilder(new ItemStack(Material.STICK)).setDisplayName(ChatColor.AQUA + "Youngling Slayer 9000")
						.addGlow().setLore(ChatColor.GRAY + "attack damage: " + ChatColor.GOLD + "+7").toItemStack()));
		selectableChampions.add(new Rex(ChatColor.BLUE + "Rex", new Ability[] {

				new RexFirstAbility(),

				new RexSecondAbility(),

				new RexThirdAbility(),

				new RexUltimate() }, 1036, 44, 35, 71, 0, 0, 0, 0.638f,
				ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_REX),
				new ItemBuilder(new ItemStack(Material.IRON_BARDING)).setDisplayName(ChatColor.AQUA + "Blaster")
						.addGlow().setLore(ChatColor.GRAY + "attack damage: " + ChatColor.GOLD + "+5").toItemStack()));
		selectableChampions.add(new Perry(ChatColor.DARK_GREEN + "Perry", new Ability[] {

				new PerryFirstAbility(),

				new PerrySecondAbility(),

				new PerryThirdAbility(),

				new PerryUltimate(),

		}, 1060, 48, 40, 74, 10, 0, 0, 72, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_PERRY),
				new ItemBuilder(new ItemStack(Material.ANVIL)).setDisplayName(ChatColor.GREEN + "Fist")
						.setLore(ChatColor.GRAY + "attack damage: " + ChatColor.GOLD + "+10").toItemStack()));
		selectableChampions.add(new Brand(ChatColor.GOLD + "Brand", new Ability[] {

				new BrandFirstAbility(),

				new BrandSecondAbility(),

				new BrandThirdAbility(),

				new BrandUltimate(),

		}, 992, 40, 35, 69, 57, 0, 0, 0.625f, ItemBuilder.getCustomTextureHead(SkullFactory.HEAD_BRAND),
				new ItemBuilder(Material.GOLD_NUGGET).setDisplayName(ChatColor.GOLD + "Molten Fire")
						.setLore(ChatColor.GRAY + "attack damage: " + ChatColor.GOLD + "+6").toItemStack()));

		selectableChampions.forEach(champion -> {

			champion.setPlugin(plugin);
			champion.setAbilityCooldownManager(abilityCooldownManager);
			champion.setSpecialEffectsManager(specialEffectsManager);
			champion.setCustomPlayerManager(customPlayerManager);
		});
	}

	public List<Champion> getSelectableChampions() {
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

	public static void init() {
		instance = new ChampionsManager();
	}

	public static ChampionsManager getInstance() {
		return instance;
	}

}
