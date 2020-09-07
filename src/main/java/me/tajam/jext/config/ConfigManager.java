package me.tajam.jext.config;

import java.io.File;

import me.tajam.jext.Log;
import me.tajam.jext.config.field.ConfigField;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

  private static final Log CONFIG_RESET = new Log().warn().t("Legacy configuration file unsupported, reset to default.");
  private static ConfigManager instance = null;
  public static ConfigManager getInstance() {
    if (instance == null) {
      instance = new ConfigManager();
    }
    return instance;
  }

  private Plugin plugin;

  private ConfigManager() {}

  public ConfigManager setPlugin(Plugin plugin) {
    this.plugin = plugin;
    return this;
  }

  public void load() {
    plugin.saveDefaultConfig();
    final FileConfiguration file = plugin.getConfig();
    if (!file.isSet(ConfigData.PATH)) {
      reset();
      return;
    }
    final ConfigurationSection section = file.getConfigurationSection(ConfigData.PATH);

    for (ConfigData.BooleanData.Path key : ConfigData.BooleanData.DataMap.keySet()) {
      ConfigField<Boolean> field = ConfigData.BooleanData.DataMap.get(key);
      field.updateData(section, Boolean.class);
    }
    
    for (ConfigData.StringData.Path key : ConfigData.StringData.DataMap.keySet()) {
      ConfigField<String> field = ConfigData.StringData.DataMap.get(key);
      field.updateData(section, String.class);
    }

    ConfigDiscManager.getInstance().load(section);
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
    CONFIG_RESET.send();
    load();
  }

}