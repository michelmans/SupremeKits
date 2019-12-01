package me.alchemi.supremekits.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.alchemi.supremekits.objects.getter.NPCKit;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCListener implements Listener {

	@EventHandler
	public void onRightClickNPC(NPCRightClickEvent e) {
		
		if (e.getNPC().data().has("supremekit")) {
			new NPCKit(e.getNPC()).onRClick(e.getClicker());
		}		
		
	}
	
	@EventHandler
	public void onLeftClickNPC(NPCLeftClickEvent e) {
		if (e.getNPC().data().has("supremekit")) {
			new NPCKit(e.getNPC()).onLClick(e.getClicker());
		}
	}
	
}
