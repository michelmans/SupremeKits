package me.alchemi.supremekits.objects.click;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
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

public class NPC extends AbstractClick implements Listener{

	Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, Messenger.formatString("Preview: " + kit.getDisplayName()));
	
	public NPC(Location loc, Kit kit) {
		super(loc, kit, NPC.class);
		
		inv.setContents(kit.getArmourContents());
		inv.addItem(kit.getInventoryContents().toArray(new ItemStack[kit.getInventoryContents().size()]));
		detectEntity();
		Bukkit.getPluginManager().registerEvents(this, Supreme.getInstance());
	}
	
	public void detectEntity() {
		for (LivingEntity le : world.getLivingEntities()) {
			if (le.getType().name().equals(section.get("entity", ""))
					&& properVector(le.getLocation().toVector()).equals(vector)) {
				le.setCustomName(Messenger.formatString(kit.getDisplayName()));
				le.setCustomNameVisible(true);
			}
		}
	}
	
	public void setEntity(LivingEntity entity) {
		section.set("entity", entity.getType().name());
		save();
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
