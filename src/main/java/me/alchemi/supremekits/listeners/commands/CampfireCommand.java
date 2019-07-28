package me.alchemi.supremekits.listeners.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.supremekits.Config.Messages;
import me.alchemi.supremekits.main;
import me.alchemi.supremekits.objects.Campfire;

public class CampfireCommand implements CommandExecutor {

	public static final String[] setAliases = new String[] {"set", "setcamp", "s"};
	public static final String[] removeAliases = new String[] {"remove", "removecamp", "r", "rem", "remcamp", "rcamp"};
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (main.getInstance().hasPermission(sender, "supremekits.campfire")) {
			if (command.getName().equals("campfire")) {
				if (args.length >= 1) {
					if (Arrays.asList(setAliases).contains(args[0])) {
						create(sender, Arrays.copyOfRange(args, 1, 4));
						return true;
					} else if (Arrays.asList(removeAliases).contains(args[0])) {
						remove(sender, Arrays.copyOfRange(args, 1, 4));
						return true;
					} else {
						main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_WRONGFORMAT.value() + command.getUsage(), sender);
						return true;
					}
				} else {
					main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_WRONGFORMAT.value() + command.getUsage(), sender);
					return true;
				}
			} else if (command.getName().equals("setcampfire")) {
				create(sender, args);
				return true;
			} else if (command.getName().equals("removecampfire")) {
				remove(sender, args);
				return true;
			} else {	
				main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_UNKNOWN.value(), sender);
			}
		}
		
		return true;
	}
	
	private void create(CommandSender sender, String[] args){
		if (main.getInstance().hasPermission(sender, "supremekits.campfire.create")) {
			if (args.length >= 3) {
				double x = 0, y = 0, z = 0;
				try {
					x = Integer.valueOf(args[0]);
					y = Integer.valueOf(args[1]);
					z = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					if (sender instanceof Player) {
						x = ((Player) sender).getLocation().getBlockX(); 
						y = ((Player) sender).getLocation().getBlockY(); 
						z = ((Player) sender).getLocation().getBlockZ(); 
					}
				}
				
				if (sender instanceof Player) main.getInstance().newCamp(new Campfire(new Location(((Player) sender).getWorld(), x, y, z)));
				else main.getInstance().newCamp(new Campfire(new Location(Bukkit.getWorlds().get(0), x, y, z)));
				
				main.getInstance().getMessenger().sendMessage(Messages.CAMPFIRE_SET.value()
						.replace("$x$", String.valueOf(x))
						.replace("$y$", String.valueOf(y))
						.replace("$z$", String.valueOf(z)), sender);
				
			} else if (sender instanceof Player) {
				double x = ((Player) sender).getLocation().getBlockX(); 
				double y = ((Player) sender).getLocation().getBlockY(); 
				double z = ((Player) sender).getLocation().getBlockZ();
				
				main.getInstance().newCamp(new Campfire(new Location(((Player) sender).getWorld(), x, y, z)));
				main.getInstance().getMessenger().sendMessage(Messages.CAMPFIRE_SET.value()
						.replace("$x$", String.valueOf(x))
						.replace("$y$", String.valueOf(y))
						.replace("$z$", String.valueOf(z)), sender);
				
			} else {
				main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_WRONGFORMAT.value() + main.getInstance().getCommand("setcampfire").getUsage(), sender);
			}
		} else {
			main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", "/setcampfire"), sender);
		}
	}
	
	private void remove(CommandSender sender, String[] args) {
		if (main.getInstance().hasPermission(sender, "supremekits.campfire.remove")) {
			if (args.length >= 3) {
				double x = 0, y = 0, z = 0;
				try {
					x = Integer.valueOf(args[0]);
					y = Integer.valueOf(args[1]);
					z = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					if (sender instanceof Player) {
						x = ((Player) sender).getLocation().getBlockX(); 
						y = ((Player) sender).getLocation().getBlockY(); 
						z = ((Player) sender).getLocation().getBlockZ(); 
					}
				}
				
				if (sender instanceof Player) main.getInstance().deleteCamp(new Location(((Player) sender).getWorld(), x, y, z));
				else main.getInstance().deleteCamp(new Location(Bukkit.getWorlds().get(0), x, y, z));
				
				main.getInstance().getMessenger().sendMessage(Messages.CAMPFIRE_REMOVED.value()
						.replace("$x$", String.valueOf(x))
						.replace("$y$", String.valueOf(y))
						.replace("$z$", String.valueOf(z)), sender);
				
			} else if (sender instanceof Player) {
				double x = ((Player) sender).getLocation().getBlockX(); 
				double y = ((Player) sender).getLocation().getBlockY(); 
				double z = ((Player) sender).getLocation().getBlockZ();
				
				main.getInstance().deleteCamp(new Location(((Player) sender).getWorld(), x, y, z));
				main.getInstance().getMessenger().sendMessage(Messages.CAMPFIRE_REMOVED.value()
						.replace("$x$", String.valueOf(x))
						.replace("$y$", String.valueOf(y))
						.replace("$z$", String.valueOf(z)), sender);
				
			} else {
				main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_WRONGFORMAT.value() + main.getInstance().getCommand("removecampfire").getUsage(), sender);
			}
		} else {
			main.getInstance().getMessenger().sendMessage(Messages.COMMANDS_NOPERMISSION.value().replace("$command$", "/removecampfire"), sender);
		}
	}

}
