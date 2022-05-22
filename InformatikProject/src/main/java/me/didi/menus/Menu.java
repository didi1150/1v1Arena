package me.didi.menus;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.didi.utilities.ItemBuilder;

public abstract class Menu implements InventoryHolder {

	protected PlayerMenuUtility playerMenuUtility;
	protected Inventory inventory;
	protected ItemStack FILLER_GLASS = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7))
			.setDisplayName(" ").toItemStack();

	public Menu(PlayerMenuUtility playerMenuUtility) {
		this.playerMenuUtility = playerMenuUtility;
	}

	public abstract String getMenuName();

	public abstract int getSlots();

	public abstract void handleMenu(InventoryClickEvent e);

	public abstract void setMenuItems();

	public void open() {
		inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

		this.setMenuItems();

		playerMenuUtility.getOwner().openInventory(inventory);
	}

	// Overridden method from the InventoryHolder interface
	@Override
	public Inventory getInventory() {
		return inventory;
	}

	// Helpful utility method to fill all remaining slots with "filler glass"
	public void setFillerGlass() {
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, FILLER_GLASS);
			}
		}
	}
}
