package me.alchemi.supremekits.listeners;

import org.bukkit.entity.Tameable;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.supremekits.main;
import me.alchemi.supremekits.meta.KitMeta;

public class EventListener implements Listener {

	 public static void onPlayerDeath(PlayerDeathEvent e) {
		 if (PersistentMeta.hasMeta(e.getEntity(), KitMeta.class)) {
			 e.getEntity().removeMetadata(KitMeta.class.getName(), main.getInstance());
			 
			 for (Tameable ent : e.getEntity().getWorld().getEntitiesByClass(Tameable.class)) {
				if (ent.isTamed() && ent.getOwner().getUniqueId().equals(e.getEntity().getUniqueId())) {
					ent.remove();
				}
			}
		 }
	 }
	
}
