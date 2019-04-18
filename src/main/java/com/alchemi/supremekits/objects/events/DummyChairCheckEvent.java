package com.alchemi.supremekits.objects.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DummyChairCheckEvent extends Event{
	Object chair;
	Block block;
	Player player;
	
	public DummyChairCheckEvent(Object chair, Player player) {
		this.chair = chair;
		this.player = player;
	}
	
	public DummyChairCheckEvent(Block block, Player player) {
		this.block = block;
		this.player = player;
	}
	
	public Object getChair() {
		return this.chair;
	}
	
	public Block getBlock() {
		return this.block;
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
