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
		obj.getScore("   ").setScore(13);
		obj.getScore("Opponent:").setScore(12);

		Team opponentHP = scoreboard.registerNewTeam("opponent");

		UUID oppositePlayer = null;
		for (UUID uuid : plugin.getAlivePlayers()) {
			if (Bukkit.getPlayer(uuid) != player)
				oppositePlayer = uuid;
		}
		opponentHP.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_GRAY);
		if (oppositePlayer != null) {
			CustomPlayer customPlayer = customPlayerManager.getPlayer(oppositePlayer);

			opponentHP.setPrefix(ChatColor.RED + "" + new DecimalFormat("#").format(customPlayer.getCurrentHealth())
					+ "/" + new DecimalFormat("#").format((customPlayer.getBaseHealth()
							+ customPlayerManager.getBonusHealth(Bukkit.getPlayer(oppositePlayer)))));
		} else {
			opponentHP.setPrefix(ChatColor.RED + "No opponent");
		}

		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_GRAY).setScore(11);

		obj.getScore(ChatColor.GOLD + "Stats").setScore(10);

		obj.getScore(ChatColor.GOLD + "Attack damage").setScore(9);

		Team attackDamage = scoreboard.registerNewTeam("AD");
		attackDamage.addEntry(ChatColor.GRAY + "" + ChatColor.BLUE);
		attackDamage.setPrefix(ChatColor.GOLD + "" + CurrentStatGetter.getInstance().getAttackDamage(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.BLUE).setScore(8);

		obj.getScore(ChatColor.GOLD + "Ability power").setScore(7);

		Team abilityPower = scoreboard.registerNewTeam("AP");
		attackDamage.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_BLUE);
		attackDamage.setPrefix(ChatColor.GOLD + "" + CurrentStatGetter.getInstance().getAttackDamage(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.BLUE).setScore(8);

		obj.getScore(ChatColor.GOLD + "Attack Speed").setScore(5);

		obj.getScore(ChatColor.GOLD + "Armor Penetration").setScore(3);

		obj.getScore(ChatColor.GOLD + "Magic Penetration").setScore(1);
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
					+ "/" + new DecimalFormat("#").format((customPlayer.getBaseHealth()
							+ customPlayerManager.getBonusHealth(Bukkit.getPlayer(oppositePlayer)))));
		}
	}
}
