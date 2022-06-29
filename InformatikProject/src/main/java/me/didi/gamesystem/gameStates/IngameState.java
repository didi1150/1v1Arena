package me.didi.gamesystem.gameStates;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.didi.champion.Champion;
import me.didi.champion.ChampionsManager;
import me.didi.champion.ability.Ability;
import me.didi.gamesystem.GameState;
import me.didi.items.CustomItemManager;
import me.didi.menus.ScoreboardHandler;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayerManager;
import me.didi.utilities.ConfigHandler;
import me.didi.utilities.ItemBuilder;

public class IngameState extends GameState {

	private ConfigHandler configHandler;
	private CustomPlayerManager customPlayerManager;
	private ChampionsManager championsManager;
	private CustomItemManager customItemManager;

	public IngameState(ConfigHandler configHandler, CustomPlayerManager customPlayerManager,
			ChampionsManager championsManager, CustomItemManager customItemManager) {
		this.configHandler = configHandler;
		this.customPlayerManager = customPlayerManager;
		this.championsManager = championsManager;
		this.customItemManager = customItemManager;
	}

	@Override
	public void start() {
		int index = 0;

		customPlayerManager.startBackgroundTask();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.closeInventory();
			player.getInventory().clear();
			Champion selectedChampion = championsManager.getSelectedChampion(player);

			if (selectedChampion == null) {
				Set<Champion> championSet = new HashSet<Champion>(championsManager.getSelectableChampions());
				championsManager.setSelectedChampion(player.getUniqueId(),
						championSet.stream().skip(new Random().nextInt(championSet.size())).findFirst().orElse(null));
			}

			Ability[] abilities = selectedChampion.getAbilities();
			for (int i = 0; i < abilities.length; i++) {
				player.getInventory().setItem(i, abilities[i].getIcon());
			}

			ItemStack autoAttackItem = selectedChampion.getAutoAttackItem();

			player.getInventory().setItem(4, autoAttackItem);
			for (int i = 5; i < 9; i++) {
				player.getInventory().setItem(i,
						new ItemBuilder(new ItemStack(Material.BARRIER)).setDisplayName(ChatColor.RED + "NA")
								.setLore(ChatColor.GRAY + "This slot is not available!").toItemStack());
			}
			customPlayerManager.addPlayer(player, selectedChampion);
			player.getInventory().setHelmet(selectedChampion.getIcon());

			if (customItemManager.getSelectedItems().containsKey(player))
				customItemManager.getSelectedItems().get(player).forEach(customItem -> {
					player.getInventory().addItem(customItem.getItemStack());
				});

			applyAttackSpeed(player);

			if (configHandler.getSpawnLocations() != null)
				player.teleport(configHandler.getSpawnLocations().get(index));

			ScoreboardHandler.getInstance().setScoreboard(player);
			index++;
		}
	}

	private void applyAttackSpeed(Player player) {
		float attackSpeed = CurrentStatGetter.getInstance().getAttackSpeed(player);
		float rounded = (float) Math.round(attackSpeed * 10) / 10;
		float amplifier = 0;
		if (rounded < 1) {
			amplifier = (1 - rounded) * 10;
			player.addPotionEffect(
					new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, (int) amplifier, false, false));
		} else if (rounded > 1) {
			amplifier = (rounded - 1) * 10;
			player.addPotionEffect(
					new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, (int) amplifier, false, false));
		}

	}

	@Override
	public void stop() {

	}

}
