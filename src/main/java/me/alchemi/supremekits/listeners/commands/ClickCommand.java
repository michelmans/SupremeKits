package me.alchemi.supremekits.listeners.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.supremekits.Config.Messages;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.objects.Kit;
import me.alchemi.supremekits.objects.getter.NPCKit;
import me.alchemi.supremekits.objects.placeholders.Stringer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class ClickCommand implements CommandExecutor {

	private final static List<String> createAliases = Arrays.asList("create", "c");
	private final static List<String> deleteAliases = Arrays.asList("delete", "d");
	private final static List<String> modifyAliases = Arrays.asList("modify", "m", "edit", "e");
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission(command.getPermission())) {
			
			if (args.length < 2) {
				sender.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()).parse(sender).create());
				return true;
			}
			
			if (createAliases.contains(args[0])) create(sender, args);
			else if (deleteAliases.contains(args[0])) delete(sender);
			else if (modifyAliases.contains(args[0])) modify(sender, args);
			else {
				sender.sendMessage(new Stringer(Messages.COMMANDS_WRONGFORMAT).command(command.getUsage()).parse(sender).create());
				return true;
			}
			
		}
		return true;
	}
	
	private void create(CommandSender sender, String[] args) {
		if (!sender.hasPermission("supremekits.clicker.create")) {
			sender.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION)
					.command("/clicker create <kit>")
					.parse(sender)
					.create());
			return;
		}
		Player player = (Player) sender;
		Kit kit = Supreme.getInstance().getKit(args[1]);
		
		boolean op = player.isOp();
		if (!op) player.setOp(true);
		player.performCommand("/npc select");
		if (!op) player.setOp(false);
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
		NPCKit.create(npc, kit);
		
		Supreme.getInstance().getMessenger().sendMessage(new Stringer(Messages.CLICKER_CREATED)
				.type("NPC")
				.x(npc.getStoredLocation().getBlockX())
				.y(npc.getStoredLocation().getBlockY())
				.z(npc.getStoredLocation().getBlockZ()), sender);
		
	}

	private void delete(CommandSender sender) {
		if (!sender.hasPermission("supremekits.clicker.delete")) {
			sender.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION)
					.command("/clicker delete")
					.parse(sender)
					.create());
			return;
		}
		Player player = (Player) sender;
		
		boolean op = player.isOp();
		if (!op) player.setOp(true);
		player.performCommand("/npc select");
		if (!op) player.setOp(false);
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
		npc.data().remove("supremekit");
		
		Supreme.getInstance().getMessenger().sendMessage(new Stringer(Messages.CLICKER_REMOVED)
				.type("NPC")
				.x(npc.getStoredLocation().getBlockX())
				.y(npc.getStoredLocation().getBlockY())
				.z(npc.getStoredLocation().getBlockZ()), sender);
	}

	private void modify(CommandSender sender, String[] args) {
		if (!sender.hasPermission("supremekits.clicker.modify")) {
			sender.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION)
					.command("/clicker modify <kit>")
					.parse(sender)
					.create());
			return;
		}
		Player player = (Player) sender;
		Kit kit = Supreme.getInstance().getKit(args[1]);
		
		boolean op = player.isOp();
		if (!op) player.setOp(true);
		player.performCommand("/npc select");
		if (!op) player.setOp(false);
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
		npc.data().remove("supremekit");
		NPCKit.create(npc, kit);
			
		Supreme.getInstance().getMessenger().sendMessage(new Stringer(Messages.CLICKER_MODIFIED)
				.type("NPC")
				.x(npc.getStoredLocation().getBlockX())
				.y(npc.getStoredLocation().getBlockY())
				.z(npc.getStoredLocation().getBlockZ()), sender);
	}
}
