package com.alchemi.supremekits.listeners.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import com.alchemi.al.configurations.Messenger;
import com.alchemi.al.configurations.SexyConfiguration;
import com.alchemi.al.objects.SexyLocation;
import com.alchemi.supremekits.Config.MESSAGES;
import com.alchemi.supremekits.main;
import com.alchemi.supremekits.objects.Campfire;
import com.alchemi.supremekits.objects.Kit;

public class SkCommand implements CommandExecutor {

	public static final String[] listAliases = new String[] {"list", "get", "kit"};
	public static final String[] createAliases = new String[] {"create", "c", "createkit"};
	public static final String[] deleteAliases = new String[] {"delete", "d", "del", "deletekit"};
	public static final String[] getAliases = new String[] {"get", "kit", "g", "getkit"};
	public static final String[] setAliases = new String[] {"set", "setinv", "setkit"};
	
	Messenger messenger = main.messenger;
	PluginCommand skCmd = main.instance.getCommand("supremekits");
	PluginCommand createCmd = main.instance.getCommand("createkit");
	PluginCommand kitCmd = main.instance.getCommand("kit");
	PluginCommand deleteCmd = main.instance.getCommand("deletekit");
	PluginCommand setCmd = main.instance.getCommand("setkitinventory");
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equals("supremekits")) {
			if (args.length < 1) {
				messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + skCmd.getUsage(), sender);
				return true;
			} else if (args.length == 1) {
				if (Arrays.asList(listAliases).contains(args[0])) {
					list(sender);
					return true;
				} else if (Arrays.asList(createAliases).contains(args[0])) {
					messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + createCmd.getUsage(), sender);
					return true; 
				} else if (Arrays.asList(deleteAliases).contains(args[0])) {
					messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + deleteCmd.getUsage(), sender);
					return true;
				} else if (Arrays.asList(deleteAliases).contains(args[0])) {
					messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + setCmd.getUsage(), sender);
					return true;
				} else if (args[0].equals("reload")) {
					if (!main.KITS_FOLDER.exists()) {
						messenger.print("&4Kits folder doesn't exist, creating...");
						main.KITS_FOLDER.mkdir();
					} else {
						messenger.print("&6Loading kits if present");
						for (File file : main.KITS_FOLDER.listFiles()) {
							if (file.isFile() && file.getName().endsWith(".yml")) {
								Kit kit = null;
								try {
									kit = new Kit(SexyConfiguration.loadConfiguration(file));
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
								if (kit != null) main.instance.kits.put(kit.getName(), kit);
							}
						}
					}
					
					if (Bukkit.getPluginManager().getPlugin("RFChairs") != null) {
						main.RFCenabled = true;
					}
					
					if (!new File(main.instance.getDataFolder(), "campfires.yml").exists()) {
						try {
							new File(main.instance.getDataFolder(), "campfires.yml").createNewFile();
						} catch (IOException e) {}
						main.CAMPFIRES = new SexyConfiguration(new File(main.instance.getDataFolder(), "campfires.yml"));
					} else {
						main.CAMPFIRES = SexyConfiguration.loadConfiguration(new File(main.instance.getDataFolder(), "campfires.yml"));
						
						for (String path : main.CAMPFIRES.getValues(false).keySet()) {
							
							ConfigurationSection sec = main.CAMPFIRES.getConfigurationSection(path);
							SexyLocation sl = new SexyLocation(sec);
							main.instance.camps.put(sl.getLocation(), new Campfire(sl.getLocation()));
							
						}
						
					}
					
				} else if (args[0].equals("save")) { 
					
					for (Kit kit : main.instance.getKits()) {
						if (!kit.isEdited()) continue;
						messenger.print("Saving: " + kit.getDisplayName());
						kit.save();
					}
					
				} else {
					messenger.sendMessage(MESSAGES.COMMANDS_UNKNOWN.value(), sender);
					return true;
				}
			} else if (args.length >= 2) {
				
				if (Arrays.asList(getAliases).contains(args[0])) { 
					if (sender instanceof Player) {
						
						get((Player) sender, Arrays.copyOfRange(args, 1, 2));
						
						
					} else {
						messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/sk getkit"), sender);
					}
					return true;
				} else if (Arrays.asList(createAliases).contains(args[0])) {
					if (sender instanceof Player) {
						
						create((Player) sender, Arrays.copyOfRange(args, 1, 3));
						
						
					} else {
						messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/sk create"), sender);
					}
					return true;
				} else if (Arrays.asList(deleteAliases).contains(args[0])) {
					
					delete(sender, Arrays.copyOfRange(args, 1, 2));
					
				} else if (Arrays.asList(setAliases).contains(args[0])) {
					
					if (sender instanceof Player) {
						
						set((Player) sender, Arrays.copyOfRange(args, 1, 2));
						
						
					} else {
						messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/sk setkit"), sender);
					}
					return true;
					
				}
				
			}
				
		} else if (command.getName().equals("createkit")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					
					create((Player) sender, Arrays.copyOfRange(args, 0, 2));
					
					
				} else {
					messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
				}
				return true;
			} else {
				messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + command.getUsage(), sender);
				return true;
			}	
		} else if (command.getName().equals("deletekit")) {
			if (args.length >= 1) {
				
				delete((Player) sender, Arrays.copyOfRange(args, 0, 1));
				
				return true;
			} else {
				messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + command.getUsage(), sender);
				return true;
			}
		} else if (command.getName().equals("kit")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					
					get((Player) sender, Arrays.copyOfRange(args, 0, 1));
					
					
				} else {
					messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
				}
				return true;
			} else {
				messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + command.getUsage(), sender);
				return true;
			}
		} else if (command.getName().equals("setkitinventory")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					
					set((Player) sender, Arrays.copyOfRange(args, 0, 1));
					
					
				} else {
					messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", command.getName()), sender);
				}
				return true;
			} else {
				messenger.sendMessage(MESSAGES.COMMANDS_WRONG_FORMAT.value() + command.getUsage(), sender);
				return true;
			}
		} else {
			messenger.sendMessage(MESSAGES.COMMANDS_UNKNOWN.value(), sender);
		}
		
		return true;
	}
	
	private void create(Player sender, String[] args) {
		
		if (main.instance.hasPermission(sender, "supremekits.create")) {
			Kit newKit = Kit.createKitArgs(sender, args);
			if (main.instance.doesKitExist(newKit)) {
				messenger.sendMessage(MESSAGES.KITS_EXISTS.value()
						.replace("$displayname$", main.instance.getKit(newKit.getName()).getDisplayName())
						.replace("$name$", newKit.getName()), sender);
			} else {
				main.instance.newKit(newKit);
				messenger.sendMessage(MESSAGES.KITS_CREATED.value()
						.replace("$displayname$", newKit.getDisplayName())
						.replace("$name$", newKit.getName()), sender);
			}
		} else {
			messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/createkit"), sender);
		}
		
	}
	
	private void delete(CommandSender sender, String[] args) {
		
		if (main.instance.hasPermission(sender, "supremekits.delete")) {
			if (main.instance.deleteKit(args[0])) {
				messenger.sendMessage(MESSAGES.KITS_DELETED.value()
						.replace("$displayname$", args[0])
						.replace("$name$", args[0]), sender);
			} else {
				messenger.sendMessage(MESSAGES.KITS_UNKNOWN.value()
						.replace("$displayname$", args[0])
						.replace("$name$", args[0]), sender);
			}
		} else {
			messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/deletekit"), sender);
		}

	}
	
	private void get(Player sender, String[] args) {
		
		if (main.instance.doesKitExist(args[0])) {
			
			Kit kit = main.instance.getKit(args[0]);
			
			if (main.instance.hasPermission(sender, "supremekits.kit.*") 
					|| main.instance.hasPermission(sender, kit.getPerm())) {
				
				for (Tameable e : ((Player)sender).getWorld().getEntitiesByClass(Tameable.class)) {
					if (e.isTamed() && e.getOwner().getUniqueId().equals(sender.getUniqueId())) {
						e.remove();
					}
				}
				
				kit.applyKit(sender);
				
				messenger.sendMessage(MESSAGES.KITS_RECEIVED.value()
						.replace("$displayname$", kit.getDisplayName())
						.replace("$name$", kit.getName()), sender);
			} else {
				messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/kit " + kit.getName()), sender);
			}
			
		} else {
			messenger.sendMessage(MESSAGES.KITS_UNKNOWN.value()
					.replace("$displayname$", args[0])
					.replace("$name$", args[0]), sender);
		}
		
	}
	
	private void set(Player sender, String[] args) {

		if (main.instance.doesKitExist(args[0])) {
		
			Kit kit = main.instance.getKit(args[0]);
			
			if (main.instance.hasPermission(sender, "supremekits.setkit")) {
				kit.setContents(sender);
				messenger.sendMessage(MESSAGES.KITS_INVENTORYSET.value()
						.replace("$displayname$", kit.getDisplayName())
						.replace("$name$", kit.getName()), sender);
			} else {
				messenger.sendMessage(MESSAGES.NO_PERMISSION.value().replace("$command$", "/setkitinventory"), sender);
			}
		
		} else {
			messenger.sendMessage(MESSAGES.KITS_UNKNOWN.value()
					.replace("$displayname$", args[0])
					.replace("$name$", args[0]), sender);
		}

	}
	
	private void list(CommandSender sender) {
		String msg = MESSAGES.KITS_LIST.value();
		for (Kit kit : main.instance.getKits()) {
			if (main.instance.hasPermission(sender, kit.getPerm())
					|| main.instance.hasPermission(sender, "supremekits.kit.*")) {
				msg += kit.getDisplayName();
				msg += "&6, ";
			}
			msg = msg.substring(0, msg.length() - 4);
		}
		messenger.sendMessage(msg, sender);
	}

}
