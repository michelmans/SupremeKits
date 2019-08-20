package me.alchemi.supremekits.objects.click;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.supremekits.main;
import me.alchemi.supremekits.objects.Kit;

public abstract class AbstractClick {

	private static final SexyConfiguration configuration = SexyConfiguration.loadConfiguration(new File(main.getInstance().getDataFolder(), "locations.yml"));
	
	protected static final Map<BlockVector, AbstractClick> registry = new HashMap<BlockVector, AbstractClick>();
	
	protected ConfigurationSection section;
	
	protected BlockVector vector;
	protected World world;
	protected Kit kit;
	
	protected AbstractClick(Location loc, Kit kit, Class<? extends AbstractClick> clazz) {
		Validate.notNull(loc, "Location cannot be null!");
		Validate.notNull(kit, "Kit cannot be null!");
		Validate.notNull(clazz, "Class cannot be null!");
		Validate.notNull(loc.getWorld(), "World cannot be null! ");
		
		this.vector = properVector(loc.toVector());
		this.world = loc.getWorld();
		this.kit = kit;
		if (configuration.contains(toID(loc))) {
			
			section = configuration.getConfigurationSection(toID(loc));
			
		} else {
			
			section = configuration.createSection(toID(loc));
			section.set("kit", kit.getName());
			section.set("class", clazz.getName());
			configuration.set(toID(loc), section);
			save();
			
		}
		registry.put(this.vector, this);
	}
	
	/**
	 * @return the loc
	 */
	public final Location getLoc() {
		return vector.toLocation(world);
	}

	/**
	 * @param loc the loc to set
	 */
	public final AbstractClick setLoc(Location loc) {
		configuration.set(toID(getLoc()), null);
		this.vector = properVector(loc.toVector());
		configuration.set(toID(loc), section);
		save();
		return this;
	}

	/**
	 * @return the kit
	 */
	public final Kit getKit() {
		return kit;
	}

	public final AbstractClick remove() {
		configuration.set(toID(getLoc()), null);
		registry.remove(vector);
		return this;
	}

	/**
	 * @param kit the kit to set
	 */
	public final AbstractClick setKit(Kit kit) {
		this.kit = kit;
		section.set("kit", kit.getName());
		configuration.set(toID(getLoc()), section);
		save();
		return this;
	}

	public static void save() {
		try {
			configuration.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<BlockVector, AbstractClick> getReg(){
		return registry;
	}

	public static boolean hasClick(Location loc) {
		return registry.containsKey(properVector(loc.toVector()));
	}
	
	public static AbstractClick getClick(Location loc) {
		if (registry.containsKey(properVector(loc.toVector()))) {
			return registry.get(properVector(loc.toVector()));
		}
		throw new IllegalArgumentException();
	}
	
	public static void reload() {
		for (AbstractClick click : registry.values()) {
			click.remove();
		}
		loadClickers();
	}
	
	protected static void removeClick(Location loc) {
		if (registry.containsKey(properVector(loc.toVector()))) {
			registry.remove(properVector(loc.toVector()));
			configuration.set(toID(loc), null);
			save();
		}
	}
	
	protected static String toID(Location loc) {
		
		return loc.getWorld().getName() + "-" + loc.getBlockX() + "-" + loc.getBlockY() + "-" + loc.getBlockZ();
	}
	
	public abstract void onClick(Player player);
	
	public abstract void onHit(Player player);
	
	public static class Factory{
		
		private Location loc;
		private Kit kit;
		private Class<? extends AbstractClick> clazz;
		
		public Factory() {}
		
		public Factory loc(Location loc) {
			this.loc = loc;
			return this;
		}
		
		public Factory kit(Kit kit) {
			this.kit = kit;
			return this;
		}
		
		public Factory clazz(Class<? extends AbstractClick> clazz) {
			this.clazz = clazz;
			return this;
		}
		
		public AbstractClick create() {
			try {
				return clazz.getConstructor(Location.class, Kit.class).newInstance(loc, kit);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	protected static BlockVector properVector(Vector vec) {
		BlockVector bv = new BlockVector();
		bv.setX(vec.getBlockX());
		bv.setY(vec.getBlockY());
		bv.setZ(vec.getBlockZ());
		return bv;
	}
	
	@SuppressWarnings("unchecked")
	public static void loadClickers() {
		try {
			configuration.load();
			
			for (String sec : configuration.getValues(false).keySet()) {
				String[] split = sec.split("-");
				
				Location l = new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]));
				Kit kit = main.getInstance().getKit(configuration.getString(sec + ".kit"));
				Class<? extends AbstractClick> clazz = (Class<? extends AbstractClick>) Class.forName(configuration.getString(sec + ".class"));
				
				new Factory().loc(l).kit(kit).clazz(clazz).create();
				
			}
			
		} catch (FileNotFoundException e) {
			
			try {
				configuration.getFile().createNewFile();
					
				}
			catch (IOException e1) {}
		
		} catch (IOException | InvalidConfigurationException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
