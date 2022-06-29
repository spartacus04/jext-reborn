package me.spartacus04.jext.configuration;

import java.io.File;
import java.io.InvalidClassException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.spartacus04.jext.configuration.ConfigUtil.MarkAsConfigFile;

public class ConfigFile extends Configuration {

  private File file;
  private ConfigSection mainSection;

  public ConfigFile(Class<?> clazz, JavaPlugin plugin) throws InvalidClassException {
    if (!clazz.isAnnotationPresent(MarkAsConfigFile.class)) {
      throw new InvalidClassException("Class not marked configuration file.");
    }
    final String fileName = clazz.getAnnotation(MarkAsConfigFile.class).value();
    final File file = new File(plugin.getDataFolder(), fileName);
    this.file = file;
    this.mainSection = new ConfigSection(clazz, YamlConfiguration.loadConfiguration(file));
  }

  @Override
  public void load() {
    this.mainSection.load();
  }

  @Override
  public void save(ConfigWriter writer) {
    writer.writeHeader(this.getClass());
    this.mainSection.save(writer);
  }

  public boolean fileExists() {
    return this.file.exists();
  }

  public ConfigWriter getWriter() {
    return new ConfigWriter(this.file);
  }

}
