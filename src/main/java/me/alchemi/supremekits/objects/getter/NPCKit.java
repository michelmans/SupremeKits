package me.alchemi.supremekits.objects.getter;

import org.bukkit.Bukkit;
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
import net.citizensnpcs.api.npc.NPC;

public class NPCKit implements Listener{

	protected Kit kit;
	
	Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, Messenger.formatString("Preview: " + kit.getDisplayName()));
	
	public NPCKit(NPC npc) {		
		inv.setContents(kit.getArmourContents());
		inv.addItem(kit.getInventoryContents().toArray(new ItemStack[kit.getInventoryContents().size()]));
		
		kit = Supreme.getInstance().getKit(npc.data().get("supremekit"));
		
		Bukkit.getPluginManager().registerEvents(this, Supreme.getInstance());
	}
	
	public static void create(net.citizensnpcs.api.npc.NPC npc, Kit kit) {
		npc.data().setPersistent("supremekit", kit.getName());
	}

	public void onLClick(Player player) {
		
		kit.applyKit(player);
		
	}

	public void onRClick(Player player) {
		
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
