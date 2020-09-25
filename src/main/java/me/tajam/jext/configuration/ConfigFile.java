package me.tajam.jext.configuration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigAnnotation.MarkAsConfigFile;

public class ConfigFile {

  private Class<?> configClass;
  private String filename;
  private boolean isValid;

  public ConfigFile(Class<?> configClass) {
    this.configClass = configClass;
    if (!configClass.isAnnotationPresent(MarkAsConfigFile.class)) {
      this.isValid = false;
    } else {
      final MarkAsConfigFile annotation = configClass.getAnnotation(MarkAsConfigFile.class);
      this.filename = annotation.value();
      this.isValid = true;
    }
  }

  public void load(JavaPlugin plugin) {
    if (!isValid) return;
    final File yml = new File(plugin.getDataFolder(), this.filename);
    if (!yml.exists()) {
      new Log().info().t(this.filename).t(" file not exists, proceed to generate a new copy.").send();
      save(plugin);
      return;
    }
    final YamlConfiguration ymlconf = YamlConfiguration.loadConfiguration(yml);
    new ConfigSection(this.configClass, ymlconf).load();
  }

  public void save(JavaPlugin plugin) {

  }

}
