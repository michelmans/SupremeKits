package com.alchemi.supremekits.listeners;

import org.bukkit.entity.Tameable;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.alchemi.al.Library;
import com.alchemi.supremekits.main;
import com.alchemi.supremekits.meta.KitMeta;

public class EventListener implements Listener {

	 public static void onPlayerDeath(PlayerDeathEvent e) {
		 if (Library.hasMeta(e.getEntity(), KitMeta.class)) {
			 e.getEntity().removeMetadata(KitMeta.class.getSimpleName(), main.instance);
			 
			 for (Tameable ent : e.getEntity().getWorld().getEntitiesByClass(Tameable.class)) {
				if (ent.isTamed() && ent.getOwner().getUniqueId().equals(e.getEntity().getUniqueId())) {
					ent.remove();
				}
			}
		 }
	 }
	
}
