package com.github.igotyou.FactoryMod;

import com.github.igotyou.FactoryMod.listeners.CompactItemListener;
import com.github.igotyou.FactoryMod.listeners.FactoryModListener;
import com.github.igotyou.FactoryMod.structures.MultiBlockStructure;
import com.github.igotyou.FactoryMod.utility.MenuBuilder;

import vg.civcraft.mc.civmodcore.ACivMod;

public class FactoryMod extends ACivMod {
	private static FactoryModManager manager;
	private static FactoryMod plugin;
	private static MenuBuilder mb;

	public void onEnable() {
		super.onEnable();
		plugin = this;
		MultiBlockStructure.initializeBlockFaceMap();
		ConfigParser cp = new ConfigParser(this);
		manager = cp.parse();
		mb = new MenuBuilder();
		manager.loadFactories();
		registerListeners();
		info("Successfully enabled");
	}

	public void onDisable() {
		manager.shutDown();
		plugin.info("Shutting down");
	}

	public static FactoryModManager getManager() {
		return manager;
	}

	public String getPluginName() {
		return "FactoryMod";
	}

	public static FactoryMod getPlugin() {
		return plugin;
	}

	private void registerListeners() {
		plugin.getServer().getPluginManager()
				.registerEvents(new FactoryModListener(manager), plugin);
		plugin.getServer()
				.getPluginManager()
				.registerEvents(
						new CompactItemListener(manager.getCompactLore()),
						plugin);
	}
	
	public static MenuBuilder getMenuBuilder() {
		return mb;
	}
}
