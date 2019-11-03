package me.alchemi.supremekits.objects;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Trident;

public class Acetone {

	public static void run(World world, OfflinePlayer player) {
		for (Entity e : world.getEntitiesByClasses(Trident.class, Arrow.class, Fireball.class, DragonFireball.class)) {
			Projectile t = (Projectile)e;
			
			if (t.getShooter() == null
					|| !(t.getShooter() instanceof Player)) t.remove();
			else if (((Player)t.getShooter()).getUniqueId().equals(player.getUniqueId())) t.remove();
			
		}
		
		for (Tameable w : world.getEntitiesByClass(Tameable.class)) {
			if (w.getOwner().getUniqueId().equals(player.getUniqueId())) w.remove();
		}
	}
	
}
