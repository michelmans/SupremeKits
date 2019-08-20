package me.alchemi.supremekits.listeners.tabcompleters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.listeners.commands.SkCommand;
import me.alchemi.supremekits.objects.Kit;

public class SkTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> tabSuggest = new ArrayList<>();
		List<String> list = new ArrayList<>();
		
		if (!(sender instanceof Player))
			return tabSuggest;
		
		if (args.length == 1 && command.getName().equals("supremekits")) {
			
			if (Supreme.getInstance().hasPermission(sender, "supremekits.createkit")) list.add("create");
			list.add("getkit");
			if (Supreme.getInstance().hasPermission(sender, "supremekits.deletekit")) list.add("delete");
			list.add("list");
			if (Supreme.getInstance().hasPermission(sender, "supremekits.setkit")) list.add("setkit");
			if (Supreme.getInstance().hasPermission(sender, "supremekits.save")) list.add("save");
			if (Supreme.getInstance().hasPermission(sender, "supremekits.reload")) list.add("reload");
				
		} else if (args.length == 2 && command.getName().equals("supremekits")) {
			
			if (Arrays.asList(SkCommand.deleteAliases).contains(args[0]) && Supreme.getInstance().hasPermission(sender, "supremekits.deletekit")) {
				for (Kit kit : Supreme.getInstance().getKits()) {
					list.add(kit.getName());
				}
			} else if (Arrays.asList(SkCommand.getAliases).contains(args[0])) {
				for (Kit kit : Supreme.getInstance().getKits()) {
					if (Supreme.getInstance().hasPermission(sender, "supremekits.kit.*") || 
							Supreme.getInstance().hasPermission(sender, kit.getPerm())) list.add(kit.getName());
				}
			} else if (Arrays.asList(SkCommand.setAliases).contains(args[0]) && Supreme.getInstance().hasPermission(sender, "supremekits.setkit")) {
				for (Kit kit : Supreme.getInstance().getKits()) {
					list.add(kit.getName());
				}
			}
			
		} else if (args.length == 1) {
			if (command.getName().equals("deletekit") && Supreme.getInstance().hasPermission(sender, "supremekits.deletekit")) {
				for (Kit kit : Supreme.getInstance().getKits()) {
					list.add(kit.getName());
				}
			} else if (command.getName().equals("kit")) {
				for (Kit kit : Supreme.getInstance().getKits()) {
					if (Supreme.getInstance().hasPermission(sender, "supremekits.kit.*") || 
							Supreme.getInstance().hasPermission(sender, kit.getPerm())) list.add(kit.getName());
				}
			} else if (command.getName().equals("setkitinventory") && Supreme.getInstance().hasPermission(sender, "supremekits.setkit")) {
				for (Kit kit : Supreme.getInstance().getKits()) {
					list.add(kit.getName());
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
