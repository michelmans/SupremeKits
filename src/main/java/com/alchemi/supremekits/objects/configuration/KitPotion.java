package com.alchemi.supremekits.objects.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.alchemi.supremekits.Config;

public class KitPotion {

	private final int amplifier;
	private final int duration;
	private final PotionEffectType effectType;
	
	public KitPotion(ConfigurationSection section) {
		amplifier = section.getInt("amplifier", 0);
		duration = section.getInt("duration");
		
		effectType = PotionEffectType.getByName(section.getString("effect"));
	}
	
	public KitPotion(PotionEffect effect) {
		amplifier = effect.getAmplifier();
		duration = effect.getDuration();
		effectType = effect.getType();
	}

	/**
	 * @return the amplifier
	 */
	public int getAmplifier() {
		return amplifier;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return the effectType
	 */
	public PotionEffectType getEffectType() {
		return effectType;
	}
	
	/**
	 * @return a {@link PotionEffect}
	 */
	public PotionEffect createEffect() {
		return new PotionEffect(effectType, duration, amplifier, true, !Config.hideEffectParticles);
	}
	
	
	public FileConfiguration toFileConfiguration() {
		FileConfiguration c = new YamlConfiguration();
		c.set("effect", effectType.getName());
		c.set("amplifier", amplifier);
		c.set("duration", duration);
		return c;
	}
	
}
