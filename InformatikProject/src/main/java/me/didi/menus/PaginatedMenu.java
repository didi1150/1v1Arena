package me.didi.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.didi.utilities.ItemBuilder;

public abstract class PaginatedMenu extends Menu {

	// Keep track of what page the menu is on
	protected int page = 0;
	// 28 is max items because with the border set below,
	// 28 empty slots are remaining.
	protected int maxItemsPerPage = 28;
	// the index represents the index of the slot
	// that the loop is on
	protected int index = 0;

	public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
	}

	// Set the border and menu buttons for the menu
	public void addMenuBorder() {
		inventory.setItem(48,
				new ItemBuilder(Material.WOOD_BUTTON).setDisplayName(ChatColor.GREEN + "Left").toItemStack());

		inventory.setItem(49, new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").toItemStack());

		inventory.setItem(50,
				new ItemBuilder(Material.WOOD_BUTTON).setDisplayName(ChatColor.GREEN + "Right").toItemStack());

		for (int i = 0; i < 10; i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, super.FILLER_GLASS);
			}
		}

		inventory.setItem(17, super.FILLER_GLASS);
		inventory.setItem(18, super.FILLER_GLASS);
		inventory.setItem(26, super.FILLER_GLASS);
		inventory.setItem(27, super.FILLER_GLASS);
		inventory.setItem(35, super.FILLER_GLASS);
		inventory.setItem(36, super.FILLER_GLASS);

		for (int i = 44; i < 54; i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, super.FILLER_GLASS);
			}
		}
	}

	public int getMaxItemsPerPage() {
		return maxItemsPerPage;
	}
}
