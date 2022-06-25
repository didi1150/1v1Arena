package me.didi.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.didi.items.CustomItem;
import me.didi.utilities.ItemBuilder;

public class ItemSelectMenu extends PaginatedMenu {

	private List<CustomItem> items;

	private Map<Player, Set<CustomItem>> selectedItems;

	private Player owner;

	public ItemSelectMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
		owner = playerMenuUtility.getOwner();

		selectedItems = new HashMap<Player, Set<CustomItem>>();
		items = new ArrayList<CustomItem>();
		// Adding items
		items.add(null);

	}

	@Override
	public String getMenuName() {
		return ChatColor.YELLOW + "Select your items";
	}

	@Override
	public int getSlots() {
		return 54;
	}

	@Override
	public void handleMenu(InventoryClickEvent event) {
		int slot = event.getSlot();
		Player player = (Player) event.getWhoClicked();

		if (event.getClickedInventory() == null)
			return;
		if (event.getCurrentItem() == null)
			return;
		if (!event.getCurrentItem().hasItemMeta())
			return;

		if (event.getCurrentItem().getType().equals(Material.BARRIER)) {

			// close inventory
			player.closeInventory();

		} else if (event.getCurrentItem().getType().equals(Material.WOOD_BUTTON)) {
			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
				if (page == 0) {
					player.sendMessage(ChatColor.GRAY + "You are already on the first page.");
				} else {
					page = page - 1;
					super.open();
				}
			} else if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())
					.equalsIgnoreCase("Right")) {
				if (!((index + 1) >= items.size())) {
					page = page + 1;
					super.open();
				} else {
					player.sendMessage(ChatColor.GRAY + "You are on the last page.");
				}
			}
		} else {
			toggleItemStatus(event.getCurrentItem(), slot);
		}

	}

	@Override
	public void setMenuItems() {
		addMenuBorder();

		for (int i = 0; i < getMaxItemsPerPage(); i++) {
			index = getMaxItemsPerPage() * page + i;
			if (index >= items.size())
				break;
			if (items.get(index) != null) {
				CustomItem customItem = items.get(index);

				ItemStack itemStack = customItem.getItemStack().clone();

				if (selectedItems.containsKey(owner) && selectedItems.get(owner).contains(customItem)) {
					List<String> lore = itemStack.getItemMeta().getLore();
					lore.add(" ");
					lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "SELECTED");

					itemStack = new ItemBuilder(itemStack).addGlow().setLore(lore).toItemStack();
				}

				inventory.addItem(itemStack);
			}
		}
	}

	private void toggleItemStatus(ItemStack itemStack, int slot) {
		boolean isSelected = false;

		CustomItem customItem = null;
		if (selectedItems.containsKey(owner)) {
			for (CustomItem element : selectedItems.get(owner)) {
				if (element.getItemStack().getItemMeta().getDisplayName()
						.equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
					isSelected = true;
					customItem = element;
					break;
				}
			}
		}
		if (isSelected) {
			selectedItems.remove(owner);
			inventory.setItem(slot, customItem.getItemStack());
		} else {

			for (CustomItem element : items) {
				if (element.getItemStack().getItemMeta().getDisplayName()
						.equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
					customItem = element;
					break;
				}
			}

			Set<CustomItem> customItems = new HashSet<CustomItem>();

			if (selectedItems.containsKey(owner)) {
				customItems = selectedItems.get(owner);
			}

			customItems.add(customItem);

			selectedItems.put(owner, customItems);
			List<String> lore = itemStack.getItemMeta().getLore();
			lore.add(" ");
			lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "SELECTED");

			inventory.setItem(slot, new ItemBuilder(itemStack).addGlow().setLore(lore).toItemStack());
		}
	}

	public Map<Player, Set<CustomItem>> getSelectedItems() {
		return selectedItems;
	}
}
