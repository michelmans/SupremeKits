package me.alchemi.supremekits.objects.click;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.configurations.Messenger;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.objects.Kit;

public class Block extends AbstractClick implements Listener {

	Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, Messenger.formatString("Preview: " + kit.getDisplayName()));
	
	public Block(Location loc, Kit kit) {
		super(loc, kit, Block.class);
		
		inv.setContents(kit.getArmourContents());
		inv.addItem(kit.getInventoryContents().toArray(new ItemStack[kit.getInventoryContents().size()]));
		Bukkit.getPluginManager().registerEvents(this, Supreme.getInstance());
	}
	
	@Override
	public void onClick(Player player) {
		
		kit.applyKit(player);
		
	}

	@Override
	public void onHit(Player player) {

		player.openInventory(inv);
		
	}

	@EventHandler
	public void onInventoryMove(InventoryMoveItemEvent e) {
		if (e.getInitiator().equals(inv) || e.getDestination().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
}
