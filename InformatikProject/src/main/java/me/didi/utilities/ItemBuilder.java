package me.didi.utilities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemBuilder {

	private ItemStack itemStack;

	private ItemMeta itemMeta;

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.itemMeta = this.itemStack.getItemMeta();
	}
	
	public ItemBuilder(Material material) {
		this.itemStack = new ItemStack(material);
		this.itemMeta = this.itemStack.getItemMeta();
	}

	public ItemBuilder setDisplayName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}

	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}

	public ItemStack toItemStack() {
		itemMeta.spigot().setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public ItemBuilder addGlow() {
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	public ItemBuilder setMaterial(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder removeGlow() {
		itemStack.removeEnchantment(Enchantment.DURABILITY);
		return this;
	}

	public static ItemStack getCustomTextureHead(String value, String displayName) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", value));
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		meta.setDisplayName(displayName);
		head.setItemMeta(meta);
		return head;
	}

	public static ItemStack getCustomTextureHead(SkullFactory skullfactory) {
		return getCustomTextureHead(skullfactory.getValue(), skullfactory.getColor() + skullfactory.getName());
	}

}
