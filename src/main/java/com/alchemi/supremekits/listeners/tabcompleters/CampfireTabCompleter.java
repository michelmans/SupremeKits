package com.alchemi.supremekits.listeners.tabcompleters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.alchemi.supremekits.main;
import com.alchemi.supremekits.listeners.commands.CampfireCommand;

public class CampfireTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> tabSuggest = new ArrayList<>();
		List<String> list = new ArrayList<>();
		
		if (!(sender instanceof Player))
			return tabSuggest;
		
		if (args.length == 1 && command.getName().equals("campfire")) {
			
			if (main.instance.hasPermission(sender, "supremekits.campfire.set")) list.add("set");
			if (main.instance.hasPermission(sender, "supremekits.campfire.remove")) list.add("remove");
				
		} else if (args.length >= 2 && command.getName().equals("campfire")) {
			
			if ((main.instance.hasPermission(sender, "supremekits.campfire.set") 
					&& Arrays.asList(CampfireCommand.setAliases).contains(args[0]))
					|| (main.instance.hasPermission(sender, "supremekits.campfire.remove") 
							&& Arrays.asList(CampfireCommand.removeAliases).contains(args[0]))) {
				Block target = ((Player)sender).getTargetBlockExact(5);
				if (args.length == 2 && target != null) {
					list.add(String.valueOf(target.getX()));
					list.add(String.valueOf(target.getX()) + " " + String.valueOf(target.getY()));
					list.add(String.valueOf(target.getX()) + " " + String.valueOf(target.getY()) + " " + String.valueOf(target.getZ()));
				} else if (args.length == 3 && target != null) {
					list.add(String.valueOf(target.getY()));
					list.add(String.valueOf(target.getY()) + " " + String.valueOf(target.getZ()));
				} else if (args.length == 4 && target != null) {
					list.add(String.valueOf(target.getZ()));
				}
				
			}
			
		} else if (args.length == 1) {
			if ((command.getName().equals("setcampfire") && main.instance.hasPermission(sender, "supremekits.campfire.set")
					|| (command.getName().equals("removecampfire") && main.instance.hasPermission(sender, "supremekits.campfire.remove"))) ) {
				Block target = ((Player)sender).getTargetBlockExact(5);
				if (args.length == 1 && target != null) {
					list.add(String.valueOf(target.getX()));
					list.add(String.valueOf(target.getX()) + " " + String.valueOf(target.getY()));
					list.add(String.valueOf(target.getX()) + " " + String.valueOf(target.getY()) + " " + String.valueOf(target.getZ()));
				} else if (args.length == 2 && target != null) {
					list.add(String.valueOf(target.getY()));
					list.add(String.valueOf(target.getY()) + " " + String.valueOf(target.getZ()));
				} else if (args.length == 3 && target != null) {
					list.add(String.valueOf(target.getZ()));
				}
			}
		}

		for (int i = list.size() - 1; i >= 0; i--)
			if(list.get(i).startsWith(args[args.length - 1]))
				tabSuggest.add(list.get(i));

		Collections.sort(tabSuggest);
		return tabSuggest;
	}

}
