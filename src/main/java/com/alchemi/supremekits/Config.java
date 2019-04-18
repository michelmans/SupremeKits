package com.alchemi.supremekits;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;

import com.alchemi.al.configurations.SexyConfiguration;

public class Config {

	public static SexyConfiguration config;
	public static SexyConfiguration messages;
	
	public static enum MESSAGES {
		NO_PERMISSION("SupremeKits.Commands.NoPermission"),
		COMMANDS_WRONG_FORMAT("SupremeKits.Commands.WrongFormat"),
		COMMANDS_UNKNOWN("SupremeKits.Commands.Unknown"), 
		KITS_UNKNOWN("SupremeKits.Kits.Unknown"),
		KITS_RECEIVED("SupremeKits.Kits.Received"), 
		KITS_POTIONSRESTORED("SupremeKits.Kits.PotionsRestored"),
		KITS_CREATED("SupremeKits.Kits.Created"), 
		KITS_EXISTS("SupremeKits.Kits.Exists"),
		KITS_DELETED("SupremeKits.Kits.Deleted"), 
		KITS_INVENTORYSET("SupremeKits.Kits.InventorySet"),
		KITS_LIST("SupremeKits.Kits.List"),
		CAMP_SET("SupremeKits.Campfire.Set"),
		CAMP_REMOVED("SupremeKits.Campfire.Removed");

		String value;
		String key;

		private MESSAGES(String key) {
			this.key = key;
		}

		public void get() {
			value = messages.getString(key, "PLACEHOLDER - STRING NOT FOUND");

		}
  
		public String value() { return value; } 
	
	}
	 
	public static List<Material> validPotionTypes = new ArrayList<Material>();
	
	public static boolean hideEffectParticles = true;
	
	public static int campfireRange = 10;
	public static int campfireTime = 10;
	
	public static void enable() throws IOException, InvalidConfigurationException {
		
		config = SexyConfiguration.loadConfiguration(main.CONFIG_FILE);
		messages = SexyConfiguration.loadConfiguration(main.MESSAGES_FILE);
		
		for (SexyConfiguration file : new SexyConfiguration[] {config, messages}) {
			int version;
			if (file.equals(config)) version = main.CONFIG_FILE_VERSION; 
			else if (file.equals(messages)) version = main.MESSAGES_FILE_VERSION; 
			else version = 0;
				  
			if(!file.getFile().exists()) main.instance.saveResource(file.getFile().getName(), false);
			  
			if(!file.isSet("File-Version-Do-Not-Edit") ||
					!file.get("File-Version-Do-Not-Edit").equals(version)) {
				
				main.messenger.print("Your $file$ is outdated! Updating...".replace("$file$", file.getFile().getName())); 
				file.load(new InputStreamReader(main.instance.getResource(file.getFile().getName())));
				file.update(SexyConfiguration.loadConfiguration(new InputStreamReader(main.instance.getResource(file.getFile().getName()))));
				file.set("File-Version-Do-Not-Edit", version);
				file.save();
				main.messenger.print("File successfully updated!");
			} 
		}
		
		for (String material : config.getStringList("validPotionTypes")) {
			validPotionTypes.add(Material.getMaterial(material));
		}
		
		hideEffectParticles = config.getBoolean("hideEffectParticles", true);
		
		campfireRange = config.getInt("campfireRange", 10);
		
		for (MESSAGES value : MESSAGES.values()) value.get();
		
	}
	
	public static void reload() {
		config = SexyConfiguration.loadConfiguration(main.CONFIG_FILE);
		messages = SexyConfiguration.loadConfiguration(main.MESSAGES_FILE);
		
		for (MESSAGES value : MESSAGES.values()) value.get();
		
	}
	
	public static void save() {
		
	}
	
}
