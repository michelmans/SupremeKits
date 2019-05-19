package com.alchemi.supremekits.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;

import com.alchemi.al.configurations.SexyConfiguration;
import com.alchemi.supremekits.Config;
import com.alchemi.supremekits.Config.MESSAGES;
import com.alchemi.supremekits.main;
import com.alchemi.supremekits.meta.KitMeta;
import com.alchemi.supremekits.objects.configuration.KitPotion;
import com.sun.istack.internal.Nullable;

public class Kit {

	private ItemStack[] armourContents = new ItemStack[4];
	private List<ItemStack> inventoryContents = new ArrayList<ItemStack>();
	
	private List<KitPotion> effects = new ArrayList<KitPotion>();
	private List<ItemStack> potions = new ArrayList<ItemStack>();
	private List<String> commands = new ArrayList<String>();
	
	private final SexyConfiguration configurationFile;
	private String name;
	private String displayName;
	
	private final Permission perm;
	
	private boolean edited = false;
	
	private Kit(Player player, String name, @Nullable String displayName) {
		configurationFile = new SexyConfiguration(new File(main.KITS_FOLDER, name + ".yml"));
		this.name = name.toLowerCase();
		if (displayName == null) this.displayName = name;
		else this.displayName = name;
		
		PlayerInventory inv = player.getInventory();
		
		armourContents = inv.getArmorContents();
		
		for (ItemStack item : inv.getContents()) {
			
			if (Arrays.asList(armourContents).contains(item) || item == null || item.getType() == Material.AIR) continue;
			
			inventoryContents.add(item.clone());
			if (Config.validPotionTypes.contains(item.getType())) potions.add(item.clone());
			
		}
		
		for (PotionEffect potion : player.getActivePotionEffects()) {
			effects.add(new KitPotion(potion));
		}
		
		perm = new Permission("supremekits.kit." + name.toLowerCase());
		
		if (Bukkit.getPluginManager().getPermission(perm.getName()) != null) Bukkit.getPluginManager().addPermission(perm);
		edited = true;
		save();
	}
	
	/**
	 * Create a new kit instance from file
	 * 
	 * @param file
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public Kit(SexyConfiguration file) throws FileNotFoundException {
		
		if (file == null || file.getFile() == null || !file.getFile().exists()) {
			throw new FileNotFoundException();
		}
		
		configurationFile = file;
		name = file.getString("name", "placeholderKit").toLowerCase();
		displayName = file.getString("displayName", name);	
	
		if (file.contains("effects")) {
			
			for (String sec : file.getConfigurationSection("effects").getValues(false).keySet()) {
				
				effects.add(new KitPotion(file.getConfigurationSection("effects." + sec)));
				
			}
			
		}
		
		if (file.contains("inventory")) {
			for (ItemStack item : (List<ItemStack>) file.getList("inventory")) {
				
				if (item == null || item.getType() == Material.AIR) continue;
				inventoryContents.add(item);
				if (Config.validPotionTypes.contains(item.getType())) potions.add(item);
				
			}
			
		}
		
		if (file.contains("armour")) {
			
			armourContents[0] = file.getItemStack("armour.helmet", null);
			armourContents[1] = file.getItemStack("armour.chestplate", null);
			armourContents[2] = file.getItemStack("armour.leggings", null);
			armourContents[3] = file.getItemStack("armour.boots", null);
			
		}
		
		if (file.contains("commands")) {
			commands = file.getStringList("commands");
		}
		
		perm = new Permission("supremekits.kit." + name.toLowerCase());
		
		if (Bukkit.getPluginManager().getPermission(perm.getName()) != null) Bukkit.getPluginManager().addPermission(perm);
		
	}
	
	/**
	 * Reload this kit from file
	 */
	@SuppressWarnings("unchecked")
	public void reload() {
		name = configurationFile.getString("name", "placeholderKit").toLowerCase();
		displayName = configurationFile.getString("displayName", name);	
	
		if (configurationFile.contains("effects")) {
			
			for (String sec : configurationFile.getConfigurationSection("effects").getValues(false).keySet()) {
				
				effects.add(new KitPotion(configurationFile.getConfigurationSection("effects." + sec)));
				
			}
			
		}
		
		if (configurationFile.contains("inventory")) {
			for (ItemStack item : (List<ItemStack>) configurationFile.getList("inventory")) {
				
				if (item == null || item.getType() == Material.AIR) continue;
				inventoryContents.add(item);
				if (Config.validPotionTypes.contains(item.getType())) potions.add(item);
				
			}
			
		}
		
		if (configurationFile.contains("armour")) {
			
			armourContents[0] = configurationFile.getItemStack("armour.helmet", null);
			armourContents[1] = configurationFile.getItemStack("armour.chestplate", null);
			armourContents[2] = configurationFile.getItemStack("armour.leggings", null);
			armourContents[3] = configurationFile.getItemStack("armour.boots", null);
			
		}
		
		if (configurationFile.contains("commands")) {
			commands = configurationFile.getStringList("commands");
		}
	}
	
	/**
	 * Create a kit with name and displayname
	 * 
	 * @param player
	 * @param name
	 * @param displayName
	 * @return a new Kit instance
	 */
	public static Kit createKit(Player player, String name, @Nullable String displayName) {
		return new Kit(player, name, displayName);
	}
	
	/**
	 * Create a kit with args instead of names
	 * 
	 * @param player
	 * @param args
	 * @return a new Kit instance, null if args are less then 1
	 */
	public static Kit createKitArgs(Player player, String...args) {
		if (args.length >= 2) {
			return new Kit(player, args[0], args[1]);
		} else if (args.length == 1) {
			return new Kit(player, args[0], args[0]);
		} else {
			return null;
		}
	}
	
	/**
	 * @return the perm
	 */
	public Permission getPerm() {
		return perm;
	}

	/**
	 * Applies the kit to the player
	 * 
	 * @param player
	 */
	public void applyKit(Player player) {
		
		PlayerInventory inv = player.getInventory();
		inv.clear();
		if (!player.getActivePotionEffects().isEmpty()) {
			for (PotionEffect pe : player.getActivePotionEffects()) {
				player.removePotionEffect(pe.getType());
			}
		}
		
		inv.setArmorContents(armourContents);
		
		for (ItemStack i : inventoryContents) {
			inv.addItem(i);
		}
		for (KitPotion kp : effects) {
			player.addPotionEffect(kp.createEffect());
		}
		
		player.removeMetadata(KitMeta.class.getSimpleName(), main.instance);
		player.setMetadata(KitMeta.class.getSimpleName(), new KitMeta(this));
		player.updateInventory();
		
		for (String cmd : commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
		}
	}
	
	public void replenishPotions(Player player) {
		
		if (!potions.isEmpty()) {
			PlayerInventory inv = player.getInventory();
			
			for (ItemStack potion : potions) {
				
				inv.remove(potion.getType());
				
			}
			
			inv.addItem(potions.toArray(new ItemStack[potions.size()]));
			
			player.updateInventory();
			
			main.messenger.sendMessage(MESSAGES.KITS_POTIONSRESTORED.value(), player);
			
		}
		
	}
	
	/**
	 * Sets the inventory contents to the kit's contents.
	 * 
	 * @param player
	 */
	public void setContents(Player player) {
		
		PlayerInventory inv = player.getInventory();
		
		armourContents = inv.getArmorContents();
		
		inventoryContents.clear();
		potions.clear();
		effects.clear();
		
		for (ItemStack item : inv.getStorageContents()) {
			
			if (Arrays.asList(armourContents).contains(item) || item == null || item.getType().equals(Material.AIR)) continue;
			inventoryContents.add(item.clone());
			if (Config.validPotionTypes.contains(item.getType())) potions.add(item.clone());
			
		}
		
		for (PotionEffect potion : player.getActivePotionEffects()) {
			effects.add(new KitPotion(potion));
		}
		
		edited = true;
		
		save();
		
	}
	
	/**
	 * Save the kit to disc.
	 */
	public void save() {
		configurationFile.set("name", name);
		configurationFile.set("displayName", displayName);
		
		configurationFile.createSection("armour");
		configurationFile.set("armour.helmet", armourContents[0]);
		configurationFile.set("armour.chestplate", armourContents[1]);
		configurationFile.set("armour.leggings", armourContents[2]);
		configurationFile.set("armour.boots", armourContents[3]);
		
		System.out.println(inventoryContents);
		configurationFile.set("inventory", inventoryContents);
		
		if (!effects.isEmpty()) {
			int i = 0;
			configurationFile.createSection("effects");
			for (KitPotion kp : effects) {
				configurationFile.createSection("effects." + String.valueOf(i), kp.toFileConfiguration().getValues(true));
				i++;
			}
		}
		
		try {
			configurationFile.save();
			edited = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete the kit.
	 */
	public void delete() {
		configurationFile.getFile().delete();
	}
	
	/**
	 * @return the potions
	 */
	public List<ItemStack> getPotions() {
		return potions;
	}

	/**
	 * @param potions the potions to set
	 */
	public void setPotions(List<ItemStack> potions) {
		this.potions = potions;
		edited = true;
	}

	/**
	 * @return the armourContents
	 */
	public ItemStack[] getArmourContents() {
		return armourContents;
	}

	/**
	 * @param armourContents the armourContents to set
	 */
	public void setArmourContents(ItemStack[] armourContents) {
		this.armourContents = armourContents;
		edited = true;
	}

	/**
	 * @return the inventoryContents
	 */
	public List<ItemStack> getInventoryContents() {
		return inventoryContents;
	}

	/**
	 * @return the effects
	 */
	public List<KitPotion> getEffects() {
		return effects;
	}

	/**
	 * @param effects the effects to set
	 */
	public void setEffects(List<KitPotion> effects) {
		this.effects = effects;
		edited = true;
	}

	/**
	 * @return the configurationFile
	 */
	public SexyConfiguration getConfigurationFile() {
		return configurationFile;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the edited
	 */
	public boolean isEdited() {
		return edited;
	}
	
}
