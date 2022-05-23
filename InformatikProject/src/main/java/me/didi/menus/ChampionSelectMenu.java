package me.didi.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.characters.Champion;

public class ChampionSelectMenu extends Menu {

	private MainClass plugin;

	public ChampionSelectMenu(PlayerMenuUtility playerMenuUtility, MainClass plugin) {
		super(playerMenuUtility);
		this.plugin = plugin;
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
	public void handleMenu(InventoryClickEvent e) {
		
	}

	@Override
	public void setMenuItems() {
		for (Champion champion : plugin.getChampionsManager().getSelectableChampions()) {
			ItemStack icon = champion.getIcon();
			this.inventory.addItem(icon);
		}

		setFillerGlass();
	}

}
