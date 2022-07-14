package me.didi.menus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.didi.items.CustomItem;
import me.didi.items.CustomItemManager;
import me.didi.utilities.ChatUtils;
import me.didi.utilities.ItemBuilder;

public class ItemSelectMenu extends PaginatedMenu {

	private List<CustomItem> items;

	private CustomItemManager customItemManager;

	public ItemSelectMenu(PlayerMenuUtility playerMenuUtility, CustomItemManager customItemManager) {
		super(playerMenuUtility);
		this.customItemManager = customItemManager;

		items = customItemManager.getCustomItems();
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
		if (event.getCurrentItem().getType() == FILLER_GLASS.getType())
			return;

		if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
			// close inventory
			player.closeInventory();
		} else if (event.getCurrentItem().getType().equals(Material.WOOD_BUTTON)) {
			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
				if (page == 0) {
					ChatUtils.sendMessageToPlayer(player, ChatColor.RED + "You are on the first page already...");
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
					ChatUtils.sendMessageToPlayer(player, ChatColor.RED + "You are on the last page.");
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

				if (customItemManager.getSelectedItems().containsKey(playerMenuUtility.getOwner())
						&& isSelected(customItem)) {
					List<String> lore = itemStack.getItemMeta().getLore();
					lore.add(" ");
					lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "SELECTED");

					ItemMeta itemMeta = itemStack.getItemMeta();
					itemMeta.setLore(lore);
					itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
					itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

					itemStack.setItemMeta(itemMeta);
				}

				inventory.addItem(itemStack);
			}
		}
	}

	private boolean isSelected(CustomItem customItem) {
		for (CustomItem c : customItemManager.getSelectedItems().get(playerMenuUtility.getOwner())) {
			if (c.getLore().equals(customItem.getLore())) {
				return true;
			}
		}
		return false;
	}

	private void toggleItemStatus(ItemStack itemStack, int slot) {
		boolean isSelected = false;

		CustomItem customItem = null;
		if (customItemManager.getSelectedItems().containsKey(playerMenuUtility.getOwner())) {
			for (CustomItem element : customItemManager.getSelectedItems().get(playerMenuUtility.getOwner())) {
				if (element.getItemStack().getItemMeta().getDisplayName()
						.contains(itemStack.getItemMeta().getDisplayName())) {
					isSelected = true;
					customItem = element;
					break;
				}
			}
		}
		if (isSelected) {
			customItemManager.getSelectedItems().get(playerMenuUtility.getOwner()).remove(customItem);
			inventory.setItem(slot, customItem.getItemStack());
		} else {

			if (customItemManager.getSelectedItems().containsKey(playerMenuUtility.getOwner())
					&& customItemManager.getSelectedItems().get(playerMenuUtility.getOwner()).size() >= 4) {
				ChatUtils.sendMessageToPlayer(playerMenuUtility.getOwner(),
						ChatColor.RED + "Du kannst nur 4 Items auswählen!");
				return;
			}

			customItem = customItemManager.isSame(itemStack, customItem);

			Set<CustomItem> customItems = new HashSet<CustomItem>();
			customItems = customItemManager.getSelectedItems().getOrDefault(playerMenuUtility.getOwner(),
					new HashSet<CustomItem>());

			customItems.add(customItem.clone());

			customItemManager.getSelectedItems().put(playerMenuUtility.getOwner(), customItems);
			List<String> lore = itemStack.getItemMeta().getLore();
			lore.add(" ");
			lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "SELECTED");

			inventory.setItem(slot, new ItemBuilder(itemStack).addGlow().setLore(lore).toItemStack());
		}
	}

}
