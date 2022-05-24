package me.didi.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.didi.MainClass;
import me.didi.characters.Champion;
import me.didi.utilities.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

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
	public void handleMenu(InventoryClickEvent event) {
		if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE)
			return;
		
		ItemStack itemStack = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			Champion champion = plugin.getChampionsManager()
					.getByName(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()));
			plugin.getChampionsManager().setSelectedChampion(player.getUniqueId(), champion);
			player.closeInventory();
		}
	}

	@Override
	public void setMenuItems() {
		for (Champion champion : plugin.getChampionsManager().getSelectableChampions()) {

			ItemStack icon = champion.getIcon();
			if (champion.equals(plugin.getChampionsManager().getSelectedChampion(this.playerMenuUtility.getOwner()))) {
				icon = new ItemBuilder(icon).addGlow().toItemStack();
			}

			this.inventory.addItem(icon);
		}

		setFillerGlass();
	}

}
