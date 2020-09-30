package me.tajam.jext.configuration;

import java.io.InvalidClassException;

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
  private ConfigFile configFile;

  private ConfigManager() {
  }

  public ConfigManager setPlugin(JavaPlugin plugin) {
    this.plugin = plugin;
    return this;
  }

  public void load() {
    try {
      configFile = new ConfigFile(ConfigYmlv1_2_2.class, this.plugin);
      if (configFile.exists()) {
        configFile.load();
        return;
      }
      configFile.save();
    } catch (InvalidClassException e) {
      e.printStackTrace();
    }
  }

  public SaveLoadable getConfigFile() throws IllegalStateException {
    if (this.configFile == null) throw new IllegalStateException("Load method never been called!");
    return this.configFile;
  }

}
