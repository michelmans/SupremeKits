package me.alchemi.supremekits.listeners.commands;

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

import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.handling.SexyLocation;
import me.alchemi.supremekits.Config.Messages;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.objects.Campfire;
import me.alchemi.supremekits.objects.Kit;
import me.alchemi.supremekits.objects.placeholders.Stringer;

public class SkCommand implements CommandExecutor {

	public static final String[] listAliases = new String[] {"list", "get", "kit"};
	public static final String[] createAliases = new String[] {"create", "c", "createkit"};
	public static final String[] deleteAliases = new String[] {"delete", "d", "del", "deletekit"};
	public static final String[] getAliases = new String[] {"get", "kit", "g", "getkit"};
	public static final String[] setAliases = new String[] {"set", "setinv", "setkit"};
	
	Messenger messenger = Supreme.getInstance().getMessenger();
	PluginCommand skCmd = Supreme.getInstance().getCommand("supremekits");
	PluginCommand createCmd = Supreme.getInstance().getCommand("createkit");
	PluginCommand kitCmd = Supreme.getInstance().getCommand("kit");
	PluginCommand deleteCmd = Supreme.getInstance().getCommand("deletekit");
	PluginCommand setCmd = Supreme.getInstance().getCommand("setkitinventory");
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equals("supremekits")) {
			if (args.length < 1) {
//				messenger.sendMessage(Messages.COMMANDS_WRONG_FORMAT.value() + skCmd.getUsage(), sender);
				list(sender);
				return true;
			} else if (args.length == 1) {
				if (Arrays.asList(listAliases).contains(args[0])) {
					list(sender);
					return true;
				} else if (Arrays.asList(createAliases).contains(args[0])) {
					messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(createCmd.getUsage()), sender);
					return true; 
				} else if (Arrays.asList(deleteAliases).contains(args[0])) {
					messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(deleteCmd.getUsage()), sender);
					return true;
				} else if (Arrays.asList(deleteAliases).contains(args[0])) {
					messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(setCmd.getUsage()), sender);
					return true;
				} else if (args[0].equals("reload")) {
					if (!Supreme.KITS_FOLDER.exists()) {
						messenger.print("&4Kits folder doesn't exist, creating...");
						Supreme.KITS_FOLDER.mkdir();
					} else {
						messenger.print("&6Loading kits if present");
						for (File file : Supreme.KITS_FOLDER.listFiles()) {
							if (file.isFile() && file.getName().endsWith(".yml")) {
								Kit kit = null;
								try {
									kit = new Kit(SexyConfiguration.loadConfiguration(file));
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
								if (kit != null) Supreme.getInstance().kits.put(kit.getName(), kit);
							}
						}
					}
					
					if (Bukkit.getPluginManager().getPlugin("RFChairs") != null) {
						Supreme.getInstance().RFCenabled = true;
					}
					
					if (!new File(Supreme.getInstance().getDataFolder(), "campfires.yml").exists()) {
						try {
							new File(Supreme.getInstance().getDataFolder(), "campfires.yml").createNewFile();
						} catch (IOException e) {}
						Supreme.getInstance().CAMPFIRES = SexyConfiguration.loadConfiguration(new File(Supreme.getInstance().getDataFolder(), "campfires.yml"));
					} else {
						Supreme.getInstance().CAMPFIRES = SexyConfiguration.loadConfiguration(new File(Supreme.getInstance().getDataFolder(), "campfires.yml"));
						
						for (String path : Supreme.getInstance().CAMPFIRES.getValues(false).keySet()) {
							
							ConfigurationSection sec = Supreme.getInstance().CAMPFIRES.getConfigurationSection(path);
							SexyLocation sl = new SexyLocation(sec);
							Supreme.getInstance().camps.put(sl.getLocation(), new Campfire(sl.getLocation()));
							
						}
						
					}
					
				} else if (args[0].equals("save")) { 
					
					for (Kit kit : Supreme.getInstance().getKits()) {
						if (!kit.isEdited()) continue;
						messenger.print("Saving: " + kit.getDisplayName());
						kit.save();
					}
					
				} else {
					messenger.sendMessage(Messages.COMMANDS_UNKNOWN.value(), sender);
					return true;
				}
			} else if (args.length >= 2) {
				
				if (Arrays.asList(getAliases).contains(args[0])) { 
					if (sender instanceof Player) {
						
						get((Player) sender, Arrays.copyOfRange(args, 1, 2));
						
						
					} else {
						messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command("/sk getkit"), sender);
					}
					return true;
				} else if (Arrays.asList(createAliases).contains(args[0])) {
					if (sender instanceof Player) {
						
						create((Player) sender, Arrays.copyOfRange(args, 1, 3));
						
						
					} else {
						messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command("/sk create"), sender);
					}
					return true;
				} else if (Arrays.asList(deleteAliases).contains(args[0])) {
					
					delete(sender, Arrays.copyOfRange(args, 1, 2));
					
				} else if (Arrays.asList(setAliases).contains(args[0])) {
					
					if (sender instanceof Player) {
						
						set((Player) sender, Arrays.copyOfRange(args, 1, 2));
						
						
					} else {
						messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command("/sk setkit"), sender);
					}
					return true;
					
				}
				
			}
				
		} else if (command.getName().equals("createkit")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					
					create((Player) sender, Arrays.copyOfRange(args, 0, 2));
					
					
				} else {
					messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command(command.getName()), sender);
				}
				return true;
			} else {
				messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()), sender);
				return true;
			}	
		} else if (command.getName().equals("deletekit")) {
			if (args.length >= 1) {
				
				delete((Player) sender, Arrays.copyOfRange(args, 0, 1));
				
				return true;
			} else {
				messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()), sender);
				return true;
			}
		} else if (command.getName().equals("kit")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					
					get((Player) sender, Arrays.copyOfRange(args, 0, 1));
					
					
				} else {
					messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()), sender);
				}
				return true;
			} else {
				messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()), sender);
				return true;
			}
		} else if (command.getName().equals("setkitinventory")) {
			if (args.length >= 1) {
				if (sender instanceof Player) {
					
					set((Player) sender, Arrays.copyOfRange(args, 0, 1));
					
					
				} else {
					messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command(command.getName()), sender);
				}
				return true;
			} else {
				messenger.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()), sender);
				return true;
			}
		} else {
			messenger.sendMessage(Messages.COMMANDS_UNKNOWN.value(), sender);
		}
		
		return true;
	}
	
	private void create(Player sender, String[] args) {
		
		if (Supreme.getInstance().hasPermission(sender, "supremekits.create")) {
			Kit newKit = Kit.createKitArgs(sender, args);
			if (Supreme.getInstance().doesKitExist(newKit)) {
				messenger.sendMessage(new Stringer(Messages.KITS_EXISTS)
						.player(sender)
						.kit(newKit)
						.kitname(newKit), sender);
			} else {
				Supreme.getInstance().newKit(newKit);
				messenger.sendMessage(new Stringer(Messages.KITS_CREATED)
						.kit(newKit)
						.kitname(newKit)
						.player(sender), sender);
			}
		} else {
			messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command("/createkit"), sender);
		}
		
	}
	
	private void delete(CommandSender sender, String[] args) {
		
		if (Supreme.getInstance().hasPermission(sender, "supremekits.delete")) {
			if (Supreme.getInstance().deleteKit(args[0])) {
				messenger.sendMessage(new Stringer(Messages.KITS_DELETED)
						.kit(args[0])
						.kitname(args[0]), sender);
			} else {
				messenger.sendMessage(new Stringer(Messages.KITS_UNKNOWN)
						.kit(args[0])
						.kitname(args[0]), sender);
			}
		} else {
			messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command("/deletekit"), sender);
		}

	}
	
	private void get(Player sender, String[] args) {
		
		if (Supreme.getInstance().doesKitExist(args[0])) {
			
			Kit kit = Supreme.getInstance().getKit(args[0]);
			
			kit.applyKit(sender);
			
		} else {
			messenger.sendMessage(new Stringer(Messages.KITS_UNKNOWN)
					.kit(args[0])
					.kitname(args[0]), sender);
		}
		
	}
	
	private void set(Player sender, String[] args) {

		if (Supreme.getInstance().doesKitExist(args[0])) {
		
			Kit kit = Supreme.getInstance().getKit(args[0]);
			
			if (Supreme.getInstance().hasPermission(sender, "supremekits.setkit")) {
				kit.setContents(sender);
				messenger.sendMessage(new Stringer(Messages.KITS_INVENTORYSET)
						.kit(kit)
						.kitname(kit), sender);
			} else {
				messenger.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION).command("/setkitinventory"), sender);
			}
		
		} else {
			messenger.sendMessage(new Stringer(Messages.KITS_UNKNOWN)
					.kit(args[0])
					.kitname(args[0]), sender);
		}

	}
	
	private void list(CommandSender sender) {
		String msg = Messages.KITS_LIST.value();
//		List<String> msgs = new ArrayList<String>();
		for (Kit kit : Supreme.getInstance().getKits()) {
			
			if (Supreme.getInstance().hasPermission(sender, kit.getPerm())
					|| Supreme.getInstance().hasPermission(sender, "supremekits.kit.*")) {
				
				msg += kit.getDisplayName() + "&6, ";
//				msgs.add(kit.getDisplayName());
				
			}
		}
		msg = msg.substring(0, msg.length() - 4);
//		messenger.sendMessages((Player) sender, msgs.toArray(new String[msgs.size()]));
		messenger.sendMessage(msg, sender);
	}

}
