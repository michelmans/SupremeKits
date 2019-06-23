package me.alchemi.supremekits.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.rifledluffy.chairs.events.ChairSitEvent;

import me.alchemi.al.objects.handling.SexyLocation;
import me.alchemi.al.objects.meta.PersistentMeta;
import me.alchemi.supremekits.Config;
import me.alchemi.supremekits.main;
import me.alchemi.supremekits.meta.KitMeta;

public class Campfire {

	private Location location;
	
	private List<ArmourStandWrapper> armourstands = new ArrayList<Campfire.ArmourStandWrapper>();
	
	private BukkitTask[] tasks = new BukkitTask[2];
	
	public Campfire(Location loc) {
		
		location = loc.add(0.5, -.4, 0.5);
		armourstands.add(new ArmourStandWrapper(loc, 0.0F));
		armourstands.add(new ArmourStandWrapper(loc, 45.0F));
		armourstands.add(new ArmourStandWrapper(loc, 90.0F));
		armourstands.add(new ArmourStandWrapper(loc, 135.0F));
		
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				if (x == 0 && z == 0) continue;
				float rotation = 0.0F;
				double x2 = x/1.6F;
				double z2 = z/1.6F;
				if ((x == -1 || x == 1) && (z == -1 || z == 1)) {
					rotation = 45.0F;
					x2 = x * Math.sqrt(Math.pow(5.0F/8.0F, 2)/2.0F);
					z2 = z * Math.sqrt(Math.pow(5.0F/8.0F, 2)/2.0F);
				}
				armourstands.add(new ArmourStandWrapper(location.clone().add(x2, .4, z2), rotation));
				armourstands.get(armourstands.size() - 1).notFirePart();
				armourstands.get(armourstands.size() - 1).getArmourstand().setHelmet(new ItemStack(Material.SKELETON_SKULL));
				armourstands.get(armourstands.size() - 1).getArmourstand().setSmall(true);
			}
		}
		location.add(0, .4, 0);
		
		Bukkit.getPluginManager().registerEvents(new CampfireListener(), main.getInstance());
		if (main.getInstance().RFCenabled) Bukkit.getPluginManager().registerEvents(new CampfireChairListener(), main.getInstance());
		
		tasks[0] = Bukkit.getScheduler().runTaskTimerAsynchronously(main.getInstance(), new Particles(), 0, 5);
		tasks[1] = Bukkit.getScheduler().runTaskTimerAsynchronously(main.getInstance(), new EntityChecker(), 0, 5);
	}
	
	public void removeStands() {
		
		for (ArmourStandWrapper aw : armourstands) aw.delete();
		
	}
	
	public void delete() {
		removeStands();
		
		tasks[0].cancel();
		tasks[1].cancel();
		
		SexyLocation sl = SexyLocation.blockSpecific(location);
		for (String path : main.getInstance().CAMPFIRES.getValues(false).keySet()) {
			
			ConfigurationSection sec = main.getInstance().CAMPFIRES.getConfigurationSection(path);
			SexyLocation sl2 = new SexyLocation(sec);
			if (sl.equals(sl2)) {
				main.getInstance().CAMPFIRES.set(path, null);
			}
			
		}
		try {
			main.getInstance().CAMPFIRES.save();
		} catch (IOException e) {}
		
	}
	
	public void save() {
		
		SexyLocation sl = SexyLocation.blockSpecific(location);
		main.getInstance().CAMPFIRES.createSection(String.valueOf(main.getInstance().CAMPFIRES.getValues(false).size()), sl.getSection().getValues(true));
		try {
			main.getInstance().CAMPFIRES.save();
		} catch (IOException e) {}
		
	}
	
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the armourstands
	 */
	public List<ArmourStandWrapper> getArmourstands() {
		return armourstands;
	}

	private class Particles implements Runnable{

		@Override
		public void run() {
			
			Random rand = new Random();
			double x = location.getBlockX() + 0.5, y = location.getBlockY(), z = location.getBlockZ() + 0.5;
			Location loc = new Location(location.getWorld(), x, y, z);
			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(rand.nextInt(25)/100.0F, 2, rand.nextInt(25)/100.0F), 0, 0, rand.nextInt(15)/100.0F, 0);
			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(-rand.nextInt(25)/100.0F, 2, -rand.nextInt(25)/100.0F), 0, 0, rand.nextInt(15)/100.0F, 0);
			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(rand.nextInt(25)/100.0F, 2, -rand.nextInt(25)/100.0F), 0, 0, rand.nextInt(15)/100.0F, 0);
			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(rand.nextInt(25)/100.0F, 2, rand.nextInt(25)/100.0F), 0, 0, rand.nextInt(15)/100.0F, 0);
			
		}
		
	}
	
	private class EntityChecker implements Runnable{
		
		@Override
		public void run() {
			
			for (ArmourStandWrapper aw : armourstands) {
				if (aw.getArmourstand().isVisible() && aw.getArmourstand().getFireTicks() <= 20) {
					
					aw.getArmourstand().setFireTicks(10000);
					
				}
			}
			
			for (Entity e : armourstands.get(0).getArmourstand().getNearbyEntities(0, 2, 0)) {
				if (e.getType().equals(EntityType.ARMOR_STAND) || e.isInvulnerable() || (e.getType().equals(EntityType.PLAYER) && ((Player)e).getGameMode().equals(GameMode.CREATIVE))) continue;
				
				else if (e.getType().equals(EntityType.DROPPED_ITEM)) {
					e.remove();
				}
				
				else if (e instanceof LivingEntity){
					
					if (e.getFireTicks() <= 0) e.setFireTicks(20);
					else e.setFireTicks(e.getFireTicks() + 20);
					
				}
			}
			
		}
		
	}
	
	private class ArmourStandWrapper {
		
		private final float rotation;
		private final ArmorStand armourstand;
		private final Location location;
		
		public ArmourStandWrapper(Location location, float rotation) {
			this.rotation = rotation;
			this.location = location.clone();
			this.location.setYaw(rotation);
			armourstand = spawn();
			setProperties();
		}
		
		public void notFirePart() {
			if (armourstand != null) {
				armourstand.setVisible(false);
				armourstand.setFireTicks(0);
			}
		}
		
		public ArmorStand spawn() {
			return location.getWorld().spawn(location, ArmorStand.class);
		}
		
		public void setProperties() {
			if (armourstand != null) {
				armourstand.setCollidable(false);
				armourstand.setGravity(false);
				armourstand.setAI(false);
				armourstand.setInvulnerable(true);
				armourstand.setVisible(true);
				armourstand.setFireTicks(10000);
			}
		}
		
		public void delete() {
			armourstand.remove();
		}

		/**
		 * 
		 * @return the rotation
		 */
		@SuppressWarnings("unused")
		public float getRotation() {
			return rotation;
		}

		/**
		 * @return the armourstand
		 */
		public ArmorStand getArmourstand() {
			return armourstand;
		}

		/**
		 * @return the location
		 */
		@SuppressWarnings("unused")
		public Location getLocation() {
			return location;
		}
		
		
		
	}

	private class CampfireListener implements Listener{
		
		private Map<Player, BukkitTask> tasks = new HashMap<Player, BukkitTask>();
		
		public CampfireListener() {
			main.getInstance().getMessenger().print("I shall start listening!");
		}
		
		@EventHandler
		public void onPlayerSneak(PlayerToggleSneakEvent e) {
			if (!PersistentMeta.hasMeta(e.getPlayer(), KitMeta.class)) return;
			
			if (e.getPlayer().getLocation().distance(location) <= Config.campfireRange) {
				if (!tasks.containsKey(e.getPlayer())) tasks.put(e.getPlayer(), Bukkit.getScheduler().runTaskTimerAsynchronously(main.getInstance(), new Runnable() {
					
					int timer = Config.campfireTime;
					
					@Override
					public void run() {
						
						timer--;
						
						if (e.getPlayer().getLocation().distance(location) > Config.campfireRange || !e.getPlayer().isSneaking()) {
							cancelTask(e.getPlayer());
						} else if (timer <= 0) {
							if (main.getInstance().hasPermission(e.getPlayer(), "supremekits.campfire.use")) ((Kit) PersistentMeta.getMeta(e.getPlayer(), KitMeta.class).value()).replenishPotions(e.getPlayer());
							cancelTask(e.getPlayer());
						}
						
					}
				}, 20, 20));
			}
		}
		
		public void cancelTask(Player player) {
			if (tasks.containsKey(player)) {
				tasks.get(player).cancel();
				tasks.remove(player);
			}
		}
		
	}
	
	private class CampfireChairListener implements Listener{
		private Map<Player, BukkitTask> tasks = new HashMap<Player, BukkitTask>();
		
		public CampfireChairListener() {
			main.getInstance().getMessenger().print("I shall start listening from my chair!");
		}
		
		@EventHandler
		public void onSit(ChairSitEvent e) {
			if (!PersistentMeta.hasMeta(e.getPlayer(), KitMeta.class)) return;
			
			if (e.getPlayer().getLocation().distance(location) <= Config.campfireRange) {
				if (!tasks.containsKey(e.getPlayer())) tasks.put(e.getPlayer(), Bukkit.getScheduler().runTaskTimerAsynchronously(main.getInstance(), new Runnable() {
					
					int timer = Config.campfireTime;
					
					@Override
					public void run() {
						
						timer--;
						if (e.getPlayer().getLocation().distance(location) > Config.campfireRange || e.getChair().getPlayer() != e.getPlayer()) {
							cancelTask(e.getPlayer());
						} else if (timer <= 0) {
							if (main.getInstance().hasPermission(e.getPlayer(), "supremekits.campfire.use")) ((Kit) PersistentMeta.getMeta(e.getPlayer(), KitMeta.class).value()).replenishPotions(e.getPlayer());
							cancelTask(e.getPlayer());
						}
						
					}
				}, 20, 20));
			}
		}
		
		public void cancelTask(Player player) {
			if (tasks.containsKey(player)) {
				tasks.get(player).cancel();
				tasks.remove(player);
			}
		}
	}
}
