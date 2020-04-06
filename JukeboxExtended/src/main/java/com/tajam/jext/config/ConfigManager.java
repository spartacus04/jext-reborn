package com.tajam.jext.config;

import java.io.File;

import com.tajam.jext.config.field.ConfigField;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

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
  private ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

  private ConfigManager() {}

  public ConfigManager setPlugin(Plugin plugin) {
    this.plugin = plugin;
    return this;
  }

  public void load() {
    plugin.saveDefaultConfig();
    final FileConfiguration file = plugin.getConfig();
    final ConfigurationSection section = file.getConfigurationSection(ConfigData.PATH);
    System.out.println(section);
    if (section == null) {
      reset();
      return;
    }
    boolean repaired = false;

    for (ConfigData.BooleanData.Path key : ConfigData.BooleanData.DataMap.keySet()) {
      ConfigField<Boolean> field = ConfigData.BooleanData.DataMap.get(key);
      boolean r = field.updateData(section, Boolean.class);
      if (!repaired) repaired = r; 
    }
    
    for (ConfigData.StringData.Path key : ConfigData.StringData.DataMap.keySet()) {
      ConfigField<String> field = ConfigData.StringData.DataMap.get(key);
      boolean r = field.updateData(section, String.class);
      if (!repaired) repaired = r; 
    }

    boolean r = ConfigDiscManager.getInstance().load(section);
    if (!repaired) repaired = r;

    if (repaired) {
      plugin.saveConfig();
      consoleSender.sendMessage(CONFIG_REPAIR);
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
    consoleSender.sendMessage(CONFIG_RESET);
    load();
  }

}