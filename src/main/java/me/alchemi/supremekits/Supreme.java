package me.alchemi.supremekits;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.base.PluginBase;
import me.alchemi.al.objects.handling.SexyLocation;
import me.alchemi.supremekits.listeners.EventListener;
import me.alchemi.supremekits.listeners.commands.CampfireCommand;
import me.alchemi.supremekits.listeners.commands.ClickCommand;
import me.alchemi.supremekits.listeners.commands.SkCommand;
import me.alchemi.supremekits.listeners.tabcompleters.CampfireTabCompleter;
import me.alchemi.supremekits.listeners.tabcompleters.ClickTabCompleter;
import me.alchemi.supremekits.listeners.tabcompleters.SkTabCompleter;
import me.alchemi.supremekits.objects.Campfire;
import me.alchemi.supremekits.objects.Kit;
import me.alchemi.supremekits.objects.click.AbstractClick;
import me.alchemi.supremekits.objects.placeholders.PapiExpansion;

public class Supreme extends PluginBase {
	
	public static File KITS_FOLDER;
	
	public SexyConfiguration CAMPFIRES;
	
	private static Supreme instance;
	
	public boolean RFCenabled = false;
	
	public Map<String, Kit> kits = new HashMap<String, Kit>();
	
	public Map<Location, Campfire> camps = new HashMap<Location, Campfire>();
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		setMessenger(new Messenger(this));
		
		KITS_FOLDER = new File(getDataFolder(), "kits");
		
		try {
			new Config();
			messenger.print("&6Configs enabled!");
		} catch (IOException | InvalidConfigurationException e) {
			System.err.println("[SupremeKits]: Could not enable config files. Disabling plugin...");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
		
		if (!KITS_FOLDER.exists()) {
			messenger.print("&4Kits folder doesn't exist, creating...");
			KITS_FOLDER.mkdir();
		} else {
			messenger.print("&6Loading kits if present");
			for (File file : KITS_FOLDER.listFiles()) {
				if (file.isFile() && file.getName().endsWith(".yml")) {
					Kit kit = null;
					try {
						kit = new Kit(SexyConfiguration.loadConfiguration(file));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					if (kit != null) kits.put(kit.getName(), kit);
				}
			}
		}
		
		messenger.print("&6Trying to load available clickers...");
		AbstractClick.loadClickers();
		
		if (getServer().getPluginManager().getPlugin("RFChairs") != null 
				|| getServer().getPluginManager().isPluginEnabled("RFChairs")) {
			RFCenabled = true;
			messenger.print("&6CHAIRSSS");
		}
		
		if (!new File(getDataFolder(), "campfires.yml").exists()) {
			try {
				new File(getDataFolder(), "campfires.yml").createNewFile();
			} catch (IOException e) {}
			CAMPFIRES = SexyConfiguration.loadConfiguration(new File(getDataFolder(), "campfires.yml"));
		} else {
			CAMPFIRES = SexyConfiguration.loadConfiguration(new File(getDataFolder(), "campfires.yml"));
			
			for (String path : CAMPFIRES.getValues(false).keySet()) {
				
				ConfigurationSection sec = CAMPFIRES.getConfigurationSection(path);
				SexyLocation sl = new SexyLocation(sec);
				camps.put(sl.getLocation(), new Campfire(sl.getLocation()));
				
			}
			
		}
		
		enableCommands();
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			if (new PapiExpansion().register()) messenger.print("&6Papi Expansion registered!");
		}
		
		messenger.print("&6DALEKS ARE SUPREME!!!");
	}

	@Override
	public void onDisable() {
		messenger.print("&6THE.. DOCTOR... IS... ESCAPING...\n&6WHAT... ARE... THESE... WORDS?\n&6EXPLAIN! EXPLAIN!");
		
		for (Kit kit : Supreme.getInstance().getKits()) {
			if (!kit.isEdited()) continue;
			messenger.print("Saving: " + kit.getDisplayName());
			kit.save();
		}
		
		for (Campfire camp : getCamps()) {
			camp.removeStands();
		}
		
		messenger.print("Kits saved.");
	}
	
	public void enableCommands() {
		getCommand("supremekits").setExecutor(new SkCommand());
		getCommand("createkit").setExecutor(new SkCommand());
		getCommand("deletekit").setExecutor(new SkCommand());
		getCommand("kit").setExecutor(new SkCommand());
		getCommand("setkitinventory").setExecutor(new SkCommand());
		getCommand("campfire").setExecutor(new CampfireCommand());
		getCommand("setcampfire").setExecutor(new CampfireCommand());
		getCommand("removecampfire").setExecutor(new CampfireCommand());
		getCommand("clicker").setExecutor(new ClickCommand());
		
		
		getCommand("supremekits").setTabCompleter(new SkTabCompleter());
		getCommand("deletekit").setTabCompleter(new SkTabCompleter());
		getCommand("kit").setTabCompleter(new SkTabCompleter());
		getCommand("setkitinventory").setTabCompleter(new SkTabCompleter());
		getCommand("campfire").setTabCompleter(new CampfireTabCompleter());
		getCommand("setcampfire").setTabCompleter(new CampfireTabCompleter());
		getCommand("removecampfire").setTabCompleter(new CampfireTabCompleter());
		getCommand("clicker").setTabCompleter(new ClickTabCompleter());
	}
	
	public boolean hasPermission(CommandSender sender, String perm) {
		return sender.isOp() | sender.hasPermission(perm) | !(sender instanceof Player);
	}
	
	public boolean hasPermission(CommandSender sender, Permission perm) {
		return sender.isOp() | sender.hasPermission(perm) | !(sender instanceof Player);
	}
	
	public void newKit(Kit kit) {
		if (!doesKitExist(kit)) kits.put(kit.getName(), kit);
	}
	
	public boolean doesKitExist(Kit kit) {
		return kits.isEmpty() ? false : kits.containsKey(kit.getName());
	}
	
	public boolean doesKitExist(String kit) {
		return kits.isEmpty() ? false : kits.containsKey(kit);
	}
	
	public boolean deleteKit(String kit) {
		if (doesKitExist(kit)) {
			getKit(kit).delete();
			kits.remove(kit);
			return true;
		}
		return false;
	}
	
	public Kit getKit(String kit) {
		return doesKitExist(kit) ? kits.get(kit) : null;
	}
	
	public Collection<Kit> getKits(){
		return kits.values();
	}
	
	public void newCamp(Campfire camp) {
		camps.put(camp.getLocation(), camp);
		camp.save();
	}
	
	public boolean doesCampExist(Campfire camp) {
		return camps.isEmpty() ? false : camps.containsKey(camp.getLocation());
	}
	
	public boolean doesCampExist(Location camp) {
		return camps.isEmpty() ? false : camps.containsKey(camp);
	}
	
	public boolean deleteCamp(Location camp) {
		if (doesCampExist(camp)) {
			getCamp(camp).delete();
			camps.remove(camp);
			return true;
		}
		return false;
	}
	
	public Campfire getCamp(Location camp) {
		return doesCampExist(camp) ? camps.get(camp) : null;
	}
	
	public Collection<Campfire> getCamps(){
		return camps.values();
	}

	public static Supreme getInstance() {
		return instance;
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
}
