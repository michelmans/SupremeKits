package com.alchemi.supremekits.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.alchemi.al.Library;
import com.alchemi.supremekits.main;
import com.alchemi.supremekits.meta.KitMeta;

public class EventListener implements Listener {

	 public static void onPlayerDeath(PlayerDeathEvent e) {
		 if (Library.hasMeta(e.getEntity(), KitMeta.class)) {
			 e.getEntity().removeMetadata(KitMeta.class.getSimpleName(), main.instance);
		 }
	 }
	
}
