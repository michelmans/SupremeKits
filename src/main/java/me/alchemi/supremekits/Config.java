package me.alchemi.supremekits;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;

import me.alchemi.al.api.MaterialWrapper;
import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.base.ConfigBase;

public class Config extends ConfigBase{

	public Config() throws FileNotFoundException, IOException, InvalidConfigurationException {
		super(main.getInstance());
		
		config = ConfigEnum.CONFIG.getConfig();
		
		for (String material : config.getStringList("validPotionTypes")) {
			validPotionTypes.add(MaterialWrapper.getWrapper(material));
		}
		
		hideEffectParticles = config.getBoolean("hideEffectParticles", true);
		
		campfireRange = config.getInt("campfireRange", 10);
		
		
	}

	public static enum ConfigEnum implements IConfigEnum{
		CONFIG(new File(main.getInstance().getDataFolder(), "config.yml"), 1),
		MESSAGES(new File(main.getInstance().getDataFolder(), "messages.yml"), 2);

		final File file;
		final int version;
		SexyConfiguration config;
		
		private ConfigEnum(File file, int version) {
			this.file = file;
			this.version = version;
			this.config = SexyConfiguration.loadConfiguration(file);
		}
		
		@Override
		public SexyConfiguration getConfig() {
			return config;
		}

		@Override
		public File getFile() {
			return file;
		}

		@Override
		public int getVersion() {
			return version;
		}
	}
	
	public static SexyConfiguration config;
	
	public static enum Messages implements IMessage{
		COMMANDS_NOPERMISSION("SupremeKits.Commands.NoPermission"),
		COMMANDS_WRONGFORMAT("SupremeKits.Commands.WrongFormat"),
		COMMANDS_UNKNOWN("SupremeKits.Commands.Unknown"),
		KITS_UNKNOWN("SupremeKits.Kits.Unknown"),
		KITS_RECEIVED("SupremeKits.Kits.Received"),
		KITS_POTIONSRESTORED("SupremeKits.Kits.PotionsRestored"),
		KITS_CREATED("SupremeKits.Kits.Created"),
		KITS_EXISTS("SupremeKits.Kits.Exists"),
		KITS_DELETED("SupremeKits.Kits.Deleted"),
		KITS_INVENTORYSET("SupremeKits.Kits.InventorySet"),
		KITS_LIST("SupremeKits.Kits.List"),
		CAMPFIRE_SET("SupremeKits.Campfire.Set"),
		CAMPFIRE_REMOVED("SupremeKits.Campfire.Removed"),
		CLICKER_CREATED("SupremeKits.Clicker.Created"),
		CLICKER_REMOVED("SupremeKits.Clicker.Removed"),
		CLICKER_MODIFIED("SupremeKits.Clicker.Modified");

		String value;
		String key;

		private Messages(String key) {
			this.key = key;
		}

		public void get() {
			value = getConfig().getString(key, "PLACEHOLDER - STRING NOT FOUND");

		}
  
		public String value() { return value; }

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.MESSAGES.getConfig();
		} 
	
	}
	 
	public static List<Material> validPotionTypes = new ArrayList<Material>();
	
	public static boolean hideEffectParticles = true;
	
	public static int campfireRange = 10;
	public static int campfireTime = 10;
	
	@Override
	protected IConfigEnum[] getConfigs() {
		return ConfigEnum.values();
	}

	@Override
	protected Set<IConfig> getEnums() {
		return new HashSet<ConfigBase.IConfig>();
	}

	@Override
	protected Set<IMessage> getMessages() {
		return new HashSet<ConfigBase.IMessage>() {
			{
				addAll(Arrays.asList(Messages.values()));
			}
		};
	}
	
}
