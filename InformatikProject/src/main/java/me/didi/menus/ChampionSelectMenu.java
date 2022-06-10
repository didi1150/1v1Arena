package me.didi.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.didi.champion.Champion;
import me.didi.champion.ChampionsManager;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class ChampionSelectMenu extends Menu {

	private ChampionsManager championsManager;

	public ChampionSelectMenu(PlayerMenuUtility playerMenuUtility, ChampionsManager championsManager) {
		super(playerMenuUtility);
		this.championsManager = championsManager;
	}

	@Override
	public String getMenuName() {
		return "Champion Select";
	}

	@Override
	public int getSlots() {
		return 9 * 5;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE)
			return;

		ItemStack itemStack = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			Champion champion = championsManager
					.getByName(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
			championsManager.setSelectedChampion(player.getUniqueId(), champion);
			ChatUtils.sendMessageToPlayer(player, ChatColor.YELLOW + "Du hast den Champion "
					+ event.getCurrentItem().getItemMeta().getDisplayName() + ChatColor.YELLOW + " ausgewählt.");
			player.closeInventory();
		}
	}

	@Override
	public void setMenuItems() {
		for (Champion champion : championsManager.getSelectableChampions()) {

			ItemStack icon = champion.getIcon();
			if (champion.equals(championsManager.getSelectedChampion(this.playerMenuUtility.getOwner()))) {
				icon = new ItemBuilder(icon).addGlow().toItemStack();
			}

			this.inventory.addItem(icon);
		}

		setFillerGlass();
	}

}
