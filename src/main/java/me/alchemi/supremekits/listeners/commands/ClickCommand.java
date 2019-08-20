package me.alchemi.supremekits.listeners.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.supremekits.Config.Messages;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.objects.Kit;
import me.alchemi.supremekits.objects.click.AbstractClick;
import me.alchemi.supremekits.objects.click.Block;
import me.alchemi.supremekits.objects.click.NPC;
import me.alchemi.supremekits.objects.placeholders.Stringer;

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
			else if (deleteAliases.contains(args[0])) delete(sender, args);
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
		RayTraceResult ray = getTarget(player);
		Kit kit = Supreme.getInstance().getKit(args[1]);
		
		if (ray != null) {
			
			Location loc;
			Class<? extends AbstractClick> clazz;
			
			if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity) {
				loc = ray.getHitEntity().getLocation();
				clazz = NPC.class;
			} else if (ray.getHitBlock() != null) {
				loc = ray.getHitBlock().getLocation();
				clazz = Block.class;
			} else {
				return;
			}
			
			AbstractClick click = new AbstractClick.Factory().loc(loc).kit(kit).clazz(clazz).create();
			
			if (ray.getHitEntity() != null){
				ray.getHitEntity().setCustomName(Messenger.formatString(kit.getDisplayName()));
				ray.getHitEntity().setCustomNameVisible(true);
				((NPC)click).setEntity((LivingEntity) ray.getHitEntity());
			}
			
			Supreme.getInstance().getMessenger().sendMessage(new Stringer(Messages.CLICKER_CREATED)
					.type(ray.getHitEntity() == null ? "Block" : "NPC")
					.x(click.getLoc().getBlockX())
					.y(click.getLoc().getBlockY())
					.z(click.getLoc().getBlockZ()), sender);
		}
	}

	private void delete(CommandSender sender, String[] args) {
		if (!sender.hasPermission("supremekits.clicker.delete")) {
			sender.sendMessage(new Stringer(Messages.COMMANDS_NOPERMISSION)
					.command("/clicker delete <kit>")
					.parse(sender)
					.create());
			return;
		}
		Player player = (Player) sender;
		RayTraceResult ray = getTarget(player);
		Location loc;
		
		if (ray == null) return;
		
		if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity) {
			loc = ray.getHitEntity().getLocation();
		} else if (ray.getHitBlock() != null) {
			loc = ray.getHitBlock().getLocation();
		} else {
			return;
		}		
		if (AbstractClick.hasClick(loc)) {
			AbstractClick click = AbstractClick.getClick(loc).remove();
			if (ray.getHitEntity() != null){
				ray.getHitEntity().setCustomNameVisible(false);
			}
			Supreme.getInstance().getMessenger().sendMessage(new Stringer(Messages.CLICKER_REMOVED)
					.type(ray.getHitEntity() == null ? "Block" : "NPC")
					.x(click.getLoc().getBlockX())
					.y(click.getLoc().getBlockY())
					.z(click.getLoc().getBlockZ()), sender);
		}
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
		RayTraceResult ray = getTarget(player);
		Kit kit = Supreme.getInstance().getKit(args[1]);
		
		if (ray != null && kit != null) {
			
			Location loc;
			Class<? extends AbstractClick> clazz;
			
			if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity) {
				
				loc = ray.getHitEntity().getLocation();
				clazz = NPC.class;
				
			} else if (ray.getHitBlock() != null) {
				loc = ray.getHitBlock().getLocation();
				clazz = Block.class;
			} else {
				return;
			}
			
			AbstractClick click = new AbstractClick.Factory().loc(loc).kit(kit).clazz(clazz).create();
			
			if (ray.getHitEntity() != null) {
				((NPC)click).setEntity((LivingEntity) ray.getHitEntity());
				ray.getHitEntity().setCustomName(Messenger.formatString(kit.getDisplayName()));
				ray.getHitEntity().setCustomNameVisible(true);
			}
			
			Supreme.getInstance().getMessenger().sendMessage(new Stringer(Messages.CLICKER_MODIFIED)
					.type(ray.getHitEntity() == null ? "Block" : "NPC")
					.x(click.getLoc().getBlockX())
					.y(click.getLoc().getBlockY())
					.z(click.getLoc().getBlockZ()), sender);
			
		}
	}

	public static RayTraceResult getTarget(Player player) {
		Vector direction = player.getEyeLocation().getDirection();
		Location loc = player.getEyeLocation().add(direction.normalize());
		
		RayTraceResult ray = loc.getWorld().rayTraceEntities(loc, direction, 9);
		
		return ray != null ? ray : loc.getWorld().rayTraceBlocks(loc, direction, 9);
	}
}
