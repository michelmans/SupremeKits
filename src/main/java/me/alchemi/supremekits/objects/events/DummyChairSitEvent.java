package me.alchemi.supremekits.objects.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DummyChairSitEvent extends Event{

	Object chair;
	Player player;
	
	public DummyChairSitEvent(Object chair, Player player) {
		this.chair = chair;
		this.player = player;
	}
	
	public Object getChair() {
		return this.chair;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	static public HandlerList getHandlerList() {
		return handlers;
}
	
}
