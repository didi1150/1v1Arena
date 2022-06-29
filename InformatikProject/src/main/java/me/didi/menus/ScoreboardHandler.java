package me.didi.menus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.didi.MainClass;
import me.didi.champion.ChampionsManager;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;

public class ScoreboardHandler {

	private static Map<Player, Scoreboard> scoreboards = new HashMap<>();
	private static final String name = ChatColor.GREEN + "InformatikProjekt";

	private static MainClass plugin;
	private static CustomPlayerManager customPlayerManager;
	private static ChampionsManager championsManager;

	private static ScoreboardHandler instance;

	public static ScoreboardHandler getInstance() {
		return instance;
	}

	public static void init(MainClass plugin, CustomPlayerManager customPlayerManager,
			ChampionsManager championsManager) {
		ScoreboardHandler.plugin = plugin;
		ScoreboardHandler.customPlayerManager = customPlayerManager;
		ScoreboardHandler.championsManager = championsManager;

		instance = new ScoreboardHandler();
	}

	public void setScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboards.put(player, scoreboard);

		Objective obj = scoreboard.registerNewObjective("infos", "aaa");
		obj.setDisplayName(name);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.getScore("Champion:").setScore(15);
		obj.getScore(championsManager.getSelectedChampion(player).getName()).setScore(14);
		obj.getScore("Opponent:").setScore(13);

		Team opponentHP = scoreboard.registerNewTeam("opponent");

		UUID oppositePlayer = null;
		for (UUID uuid : plugin.getAlivePlayers()) {
			if (Bukkit.getPlayer(uuid) != player)
				oppositePlayer = uuid;
		}
		opponentHP.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_GRAY);
		if (oppositePlayer != null) {
			CustomPlayer customPlayer = customPlayerManager.getPlayer(oppositePlayer);
			if (customPlayer != null) {
				opponentHP.setPrefix(ChatColor.RED + "" + new DecimalFormat("#").format(customPlayer.getCurrentHealth())
						+ "/" + new DecimalFormat("#").format(CurrentStatGetter.getInstance().getMaxHealth(player)));
			} else
				opponentHP.setPrefix(ChatColor.RED + "No opponent");
		} else {
			opponentHP.setPrefix(ChatColor.RED + "No opponent");
		}

		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_GRAY).setScore(12);
		
		obj.getScore(ChatColor.AQUA + "Your Stats:").setScore(11);
		
		obj.getScore(ChatColor.WHITE + "Attack damage:").setScore(10);

		Team attackDamage = scoreboard.registerNewTeam("AD");
		attackDamage.addEntry(ChatColor.GRAY + "" + ChatColor.BLUE);
		attackDamage.setPrefix(ChatColor.GOLD + "" + CurrentStatGetter.getInstance().getAttackDamage(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.BLUE).setScore(9);

		obj.getScore(ChatColor.WHITE + "Ability power:").setScore(8);

		Team abilityPower = scoreboard.registerNewTeam("AP");
		abilityPower.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_BLUE);
		abilityPower.setPrefix(ChatColor.DARK_PURPLE + "" + CurrentStatGetter.getInstance().getAbilityPower(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_BLUE).setScore(7);

		obj.getScore(ChatColor.WHITE + "Attack Speed:").setScore(6);

		Team attackSpeed = scoreboard.registerNewTeam("AS");
		attackSpeed.addEntry(ChatColor.GRAY + "" + ChatColor.AQUA);
		attackSpeed.setPrefix(ChatColor.GOLD + "" + CurrentStatGetter.getInstance().getAttackSpeed(player) + "%");
		obj.getScore(ChatColor.GRAY + "" + ChatColor.AQUA).setScore(5);

		obj.getScore(ChatColor.WHITE + "Armor Penetration:").setScore(4);

		Team armorPenetration = scoreboard.registerNewTeam("ArmorPen");
		armorPenetration.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_AQUA);
		armorPenetration.setPrefix(ChatColor.RED + "" + CurrentStatGetter.getInstance().getArmorPenetration(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_AQUA).setScore(3);

		obj.getScore(ChatColor.WHITE + "Magic Penetration:").setScore(2);

		Team magicPenetration = scoreboard.registerNewTeam("magicPen");
		magicPenetration.addEntry(ChatColor.GRAY + "" + ChatColor.BLACK);
		magicPenetration
				.setPrefix(ChatColor.DARK_PURPLE + "" + CurrentStatGetter.getInstance().getMagicPenetration(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.BLACK).setScore(1);
		// INFORMATIKPROJEKT
		// Champion: 15
		// name 14
		// 13
		// Opponent health 12
		// currentHealth / maxHealth 11
		// Stats:10
		// attack damage: 9
		// 0 8
		// ability power:
		// 0
		// attack speed:
		// 0
		// armor penetration:
		// 0
		// magic penetration:
		// 0
		player.setScoreboard(scoreboard);
	}

	public void updateOpponentHealth(Player player) {
		Scoreboard scoreboard = scoreboards.get(player);

		Team opponentHP = scoreboard.getTeam("opponent");
		UUID oppositePlayer = null;
		for (UUID uuid : plugin.getAlivePlayers()) {
			if (Bukkit.getPlayer(uuid) != player)
				oppositePlayer = uuid;
		}

		if (oppositePlayer != null) {
			CustomPlayer customPlayer = customPlayerManager.getPlayer(oppositePlayer);
			opponentHP.setPrefix(ChatColor.RED + "" + new DecimalFormat("#").format(customPlayer.getCurrentHealth())
					+ "/" + new DecimalFormat("#").format(CurrentStatGetter.getInstance().getMaxHealth(player)));
		}
	}
}
