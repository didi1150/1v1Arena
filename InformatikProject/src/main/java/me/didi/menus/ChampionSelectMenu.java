package me.didi.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public class ChampionSelectMenu extends Menu {

	public ChampionSelectMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
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

	}

}
