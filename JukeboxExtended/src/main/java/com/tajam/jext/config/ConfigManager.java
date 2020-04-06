package com.tajam.jext.config;

import java.io.File;
import java.util.logging.Logger;

import com.tajam.jext.config.field.ConfigField;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class ConfigManager {

  private static final String CONFIG_RESET = ChatColor.YELLOW + "Legacy configuration file unsupported, reset to default.";
  private static final String CONFIG_REPAIR = ChatColor.YELLOW + "Configuration file contains missing field(s), repaired.";
  private static ConfigManager instance = null;
  public static ConfigManager getInstance() {
    if (instance == null) {
      instance = new ConfigManager();
    }
    return instance;
  }

  private Plugin plugin;
  private Logger logger;

  private ConfigManager() {}

  public ConfigManager setPlugin(Plugin plugin) {
    this.plugin = plugin;
    this.logger = plugin.getLogger();
    return this;
  }

  public void load() {
    final FileConfiguration file = plugin.getConfig();
    final ConfigurationSection section = file.getConfigurationSection(ConfigData.PATH);
    if (section == null) {
      reset();
      load();
      return;
    }
    boolean repaired = false;

    for (ConfigData.BooleanData.Path key : ConfigData.BooleanData.DataMap.keySet()) {
      ConfigField<Boolean> field = ConfigData.BooleanData.DataMap.get(key);
      repaired = field.updateData(section, Boolean.class);
    }
    
    for (ConfigData.StringData.Path key : ConfigData.StringData.DataMap.keySet()) {
      ConfigField<String> field = ConfigData.StringData.DataMap.get(key);
      repaired = field.updateData(section, String.class);
    }

    repaired = ConfigDiscManager.getInstance().load(section);

    if (repaired) {
      plugin.saveConfig();
      logger.warning(CONFIG_REPAIR);
    }
  }

  public String getStringData(ConfigData.StringData.Path path) {
    return ConfigData.StringData.DataMap.get(path).getData();
  }

  public Boolean getBooleanData(ConfigData.BooleanData.Path path) {
    return ConfigData.BooleanData.DataMap.get(path).getData();
  }

  private void reset() {
    File file = new File(plugin.getDataFolder(), "config.yml");
    file.delete();
    plugin.saveDefaultConfig();
    plugin.reloadConfig();
    logger.warning(CONFIG_RESET);
  }

}