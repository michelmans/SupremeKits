package me.alchemi.supremekits.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.meta.KitMeta;
import me.alchemi.supremekits.objects.Acetone;

public class EventListener implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (PersistentMeta.hasMeta(e.getEntity(), KitMeta.class)) {
			e.getEntity().removeMetadata(KitMeta.class.getName(), Supreme.getInstance());
		}
		
		Acetone.run(e.getEntity().getWorld(), e.getEntity());
	}
	
}
