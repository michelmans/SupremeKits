package me.alchemi.supremekits.listeners.tabcompleters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.objects.Kit;

public class ClickTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> tabSuggest = new ArrayList<>();
		List<String> list = new ArrayList<>();
		
		if (!(sender instanceof Player || sender.hasPermission(command.getPermission())))
			return tabSuggest;
		
		if (args.length == 1) {
			
			if (sender.hasPermission("supremekits.clicker.create")) list.add("create");
			if (sender.hasPermission("supremekits.clicker.delete")) list.add("delete");
			if (sender.hasPermission("supremekits.clicker.modify")) list.add("modify");
				
		} else if (args.length == 2) {
			
			for (Kit kit : Supreme.getInstance().getKits()) {
				list.add(kit.getName());
			}
			
		}

		for (int i = list.size() - 1; i >= 0; i--)
			if(list.get(i).startsWith(args[args.length - 1]))
				tabSuggest.add(list.get(i));

		Collections.sort(tabSuggest);
		return tabSuggest;
	}

}
