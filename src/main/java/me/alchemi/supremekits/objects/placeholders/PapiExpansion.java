package me.alchemi.supremekits.objects.placeholders;

import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.meta.KitMeta;
import me.alchemi.supremekits.objects.Kit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PapiExpansion extends PlaceholderExpansion{

	@Override
	public boolean canRegister() {
		return true;
	}
	
	@Override
	public String getAuthor() {
		return "Alchemi";
	}

	@Override
	public String getIdentifier() {
		return Supreme.getInstance().getName();
	}

	@Override
	public String getVersion() {
		return Supreme.getInstance().getDescription().getVersion();
	}
	
	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String id) {
		
		if (id.equals("kit")) {
			return PersistentMeta.hasMeta(p, KitMeta.class) ? ((Kit)PersistentMeta.getMeta(p, KitMeta.class).value()).getDisplayName() : "null";
		} else if (id.equals("kits")) {
			return Supreme.getInstance().getKits().stream().map(Kit -> Kit.getDisplayName()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "");
		}
		
		return null;
	}
	
	@Override
	public String onRequest(OfflinePlayer p, String id) {
		if (id.equals("kits")) {
			return Supreme.getInstance().getKits().stream().map(Kit -> Kit.getDisplayName()).collect(Collectors.toList()).toString().replace("[", "").replace("]", "");
		}
		return null;
	}
	
}
