package me.didi.menus;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.didi.champion.ChampionsManager;
import me.didi.player.CurrentStatGetter;
import me.didi.player.CustomPlayer;
import me.didi.player.CustomPlayerManager;

public class ScoreboardHandler {

	private Map<Player, Scoreboard> scoreboards = new HashMap<>();
	private static final String name = ChatColor.GREEN + "InformatikProjekt";

	private CustomPlayerManager customPlayerManager;
	private ChampionsManager championsManager;

	private static ScoreboardHandler instance;

	public static ScoreboardHandler getInstance() {
		return instance;
	}

	public static void init(CustomPlayerManager customPlayerManager, ChampionsManager championsManager) {
		instance = new ScoreboardHandler(customPlayerManager, championsManager);
	}

	public ScoreboardHandler(CustomPlayerManager customPlayerManager, ChampionsManager championsManager) {
		this.customPlayerManager = customPlayerManager;
		this.championsManager = championsManager;
	}

	public void setScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboards.put(player, scoreboard);

		Objective healthBar = scoreboard.registerNewObjective("healthBar", "healthBar");
		healthBar.setDisplaySlot(DisplaySlot.BELOW_NAME);

		CustomPlayer customPlayer = customPlayerManager.getPlayer(player);
		if (customPlayer != null) {
			CustomPlayer oppositePlayer = null;
			for (Map.Entry<Player, CustomPlayer> entry : customPlayerManager.getPlayers().entrySet()) {
				if (entry.getValue().getUuid() != customPlayer.getUuid())
					oppositePlayer = entry.getValue();
			}
			if (oppositePlayer != null) {
				healthBar.setDisplayName(
						ChatColor.RED + "" + new DecimalFormat("#").format(oppositePlayer.getCurrentHealth()) + "/"
								+ new DecimalFormat("#").format(CurrentStatGetter.getInstance()
										.getMaxHealth(Bukkit.getPlayer(oppositePlayer.getUuid()))));
			}
		}

		Objective obj = scoreboard.registerNewObjective("infos", "aaa");
		obj.setDisplayName(name);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.getScore("Champion:").setScore(15);
		obj.getScore(championsManager.getSelectedChampion(player).getName()).setScore(14);

		obj.getScore(ChatColor.AQUA + "Your Stats:").setScore(13);

		obj.getScore(ChatColor.WHITE + "Attack damage:").setScore(12);

		Team attackDamage = scoreboard.registerNewTeam("AD");
		attackDamage.addEntry(ChatColor.GRAY + "" + ChatColor.BLUE);
		attackDamage.setPrefix(ChatColor.GOLD + "" + CurrentStatGetter.getInstance().getAttackDamage(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.BLUE).setScore(11);

		obj.getScore(ChatColor.WHITE + "Ability power:").setScore(10);

		Team abilityPower = scoreboard.registerNewTeam("AP");
		abilityPower.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_BLUE);
		abilityPower.setPrefix(ChatColor.DARK_PURPLE + "" + CurrentStatGetter.getInstance().getAbilityPower(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_BLUE).setScore(9);

		obj.getScore(ChatColor.WHITE + "Attack Speed:").setScore(8);

		Team attackSpeed = scoreboard.registerNewTeam("AS");
		attackSpeed.addEntry(ChatColor.GRAY + "" + ChatColor.AQUA);
		attackSpeed.setPrefix(ChatColor.GOLD + "" + CurrentStatGetter.getInstance().getAttackSpeed(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.AQUA).setScore(7);

		obj.getScore(ChatColor.WHITE + "Armor Penetration:").setScore(6);

		Team armorPenetration = scoreboard.registerNewTeam("ArmorPen");
		armorPenetration.addEntry(ChatColor.GRAY + "" + ChatColor.DARK_AQUA);
		armorPenetration.setPrefix(ChatColor.RED + "" + CurrentStatGetter.getInstance().getArmorPenetration(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.DARK_AQUA).setScore(5);

		obj.getScore(ChatColor.WHITE + "Magic Penetration:").setScore(4);

		Team magicPenetration = scoreboard.registerNewTeam("magicPen");
		magicPenetration.addEntry(ChatColor.GRAY + "" + ChatColor.BLACK);
		magicPenetration
				.setPrefix(ChatColor.DARK_PURPLE + "" + CurrentStatGetter.getInstance().getMagicPenetration(player));
		obj.getScore(ChatColor.GRAY + "" + ChatColor.BLACK).setScore(3);

		obj.getScore(ChatColor.WHITE + "Movement Speed:").setScore(2);

		Team movementSpeed = scoreboard.registerNewTeam("speed");
		movementSpeed.addEntry(ChatColor.DARK_AQUA + "" + ChatColor.GOLD);
		int speed = (int) (player.getWalkSpeed() * 100);
		movementSpeed.setPrefix("" + speed);
		obj.getScore(ChatColor.DARK_AQUA + "" + ChatColor.GOLD).setScore(1);
		// INFORMATIKPROJEKT
		// Champion: 15
		// name 14
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
		// speed
		// 0
		player.setScoreboard(scoreboard);
	}

	public void updatePlayerHealth(Player player) {
		Scoreboard scoreboard = scoreboards.get(player);
		Objective healthBar = scoreboard.getObjective(DisplaySlot.BELOW_NAME);

		CustomPlayer customPlayer = customPlayerManager.getPlayer(player);
		if (customPlayer != null) {
			CustomPlayer oppositePlayer = null;
			for (Map.Entry<Player, CustomPlayer> entry : customPlayerManager.getPlayers().entrySet()) {
				if (entry.getValue().getUuid() != customPlayer.getUuid())
					oppositePlayer = entry.getValue();
			}
			if (oppositePlayer != null) {
				healthBar.setDisplayName(
						ChatColor.RED + "" + new DecimalFormat("#").format(oppositePlayer.getCurrentHealth()) + "/"
								+ new DecimalFormat("#").format(CurrentStatGetter.getInstance()
										.getMaxHealth(Bukkit.getPlayer(oppositePlayer.getUuid()))));
			}
		}
	}

	public void updateMoveSpeed(Player player) {

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (pl != player) {
				Scoreboard scoreboard = scoreboards.get(pl);
				if(scoreboard == null) return;
				Team movementSpeed = scoreboard.getTeam("speed");

				CustomPlayer customPlayer = customPlayerManager.getPlayer(player);
				if (customPlayer != null) {
					CustomPlayer oppositePlayer = customPlayerManager.getPlayer(pl);
					if (oppositePlayer != null) {
						int speed = (int) (player.getWalkSpeed() * 100);
						movementSpeed.setPrefix("" + speed);
					}
				}

			}
		}

	}
}
