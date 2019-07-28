package me.alchemi.supremekits.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.supremekits.main;
import me.alchemi.supremekits.meta.KitMeta;
import me.alchemi.supremekits.objects.click.AbstractClick;

public class EventListener implements Listener {

	private static BukkitTask coolTask;
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (PersistentMeta.hasMeta(e.getEntity(), KitMeta.class)) {
			e.getEntity().removeMetadata(KitMeta.class.getName(), main.getInstance());
			
			for (Tameable ent : e.getEntity().getWorld().getEntitiesByClass(Tameable.class)) {
				if (ent.isTamed() && ent.getOwner().getUniqueId().equals(e.getEntity().getUniqueId())) {
					ent.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityHit(EntityDamageEvent e) {
		if (AbstractClick.hasClick(e.getEntity().getLocation())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (AbstractClick.hasClick(e.getEntity().getLocation())) {
				AbstractClick.getClick(e.getEntity().getLocation()).onHit((Player) e.getDamager());
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e) {
	
		if (AbstractClick.hasClick(e.getRightClicked().getLocation())) {
			if (coolTask == null) coolTask = Bukkit.getScheduler().runTask(main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					AbstractClick.getClick(e.getRightClicked().getLocation()).onClick(e.getPlayer());
					coolTask = null;
					
				}
			});
		}
	}
	
	@EventHandler
	public void onBlockClick(PlayerInteractEvent e) {
		
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK)
				&& AbstractClick.hasClick(e.getClickedBlock().getLocation())) {
			
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && coolTask == null) coolTask = Bukkit.getScheduler().runTaskAsynchronously(main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					AbstractClick.getClick(e.getClickedBlock().getLocation()).onClick(e.getPlayer());
					coolTask = null;
					
				}
			});
			
			else if (e.getAction() == Action.LEFT_CLICK_BLOCK) AbstractClick.getClick(e.getClickedBlock().getLocation()).onHit(e.getPlayer());
		} 
	}
	
	
}
