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

		obj.getScore("Champion:").setScore(5);
		obj.getScore(championsManager.getSelectedChampion(player).getName()).setScore(4);
		obj.getScore("   ").setScore(3);
		obj.getScore("Opponent health:").setScore(2);

		Team opponentHP = scoreboard.registerNewTeam("opponentHP");

		UUID oppositePlayer = null;
		for (UUID uuid : plugin.getAlivePlayers()) {
			if (Bukkit.getPlayer(uuid) != player)
				oppositePlayer = uuid;
		}

		if (oppositePlayer == null)
			return;

		CustomPlayer customPlayer = customPlayerManager.getPlayer(oppositePlayer);
		opponentHP.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_GRAY);
		opponentHP.setPrefix(ChatColor.RED + "" + new DecimalFormat("#").format(customPlayer.getCurrentHealth()) + "/"
				+ new DecimalFormat("#").format((customPlayer.getBaseHealth()
						+ customPlayerManager.getBonusHealth(Bukkit.getPlayer(oppositePlayer)))));

		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_GRAY).setScore(1);
		// INFORMATIKPROJEKT
		// Champion:
		// name
		//
		// Opponent health
		// currentHealth / maxHealth
		player.setScoreboard(scoreboard);
	}

	public void updateScoreboard(Player player) {
		Scoreboard scoreboard = scoreboards.get(player);

		Team opponentHP = scoreboard.getTeam("opponentHP");
		UUID oppositePlayer = null;
		for (UUID uuid : plugin.getAlivePlayers()) {
			if (Bukkit.getPlayer(uuid) != player)
				oppositePlayer = uuid;
		}

		if (oppositePlayer == null)
			return;
		CustomPlayer customPlayer = customPlayerManager.getPlayer(oppositePlayer);
		opponentHP.setPrefix(ChatColor.RED + "" + new DecimalFormat("#").format(customPlayer.getCurrentHealth()) + "/"
				+ new DecimalFormat("#").format((customPlayer.getBaseHealth()
						+ customPlayerManager.getBonusHealth(Bukkit.getPlayer(oppositePlayer)))));
	}
}