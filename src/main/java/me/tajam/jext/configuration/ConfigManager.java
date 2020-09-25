package me.tajam.jext.configuration;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
  
  private static ConfigManager instance = null;
  public static ConfigManager getInstance() {
    if (instance == null) {
      instance = new ConfigManager();
    }
    return instance;
  }

  private JavaPlugin plugin;

  private ConfigManager() {}

  public ConfigManager setPlugin(JavaPlugin plugin) {
    this.plugin = plugin;
    return this;
  }

  public void load() {
    
  }

}
