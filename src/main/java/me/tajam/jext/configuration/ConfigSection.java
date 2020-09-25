package me.tajam.jext.configuration;

import java.lang.reflect.Field;

import org.bukkit.configuration.ConfigurationSection;

import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigAnnotation.MarkAsConfigFile;
import me.tajam.jext.configuration.ConfigAnnotation.MarkAsConfigSection;

public class ConfigSection {
  
  private Class<?> sectionClass;
  private ConfigurationSection section;

  public ConfigSection(Class<?> sectionClass, ConfigurationSection section) {
    this.sectionClass = sectionClass;
    this.section = section;
  }

  public void load() {
    if (!(sectionClass.isAnnotationPresent(MarkAsConfigFile.class) || sectionClass.isAnnotationPresent(MarkAsConfigSection.class))) return;
    for (final Class<?> clazz : this.sectionClass.getDeclaredClasses()) {
      if (clazz.isAnnotationPresent(MarkAsConfigSection.class)) {
        final String name = clazz.getSimpleName().toLowerCase().replace('_', '-');
        final ConfigurationSection innerSection = this.section.getConfigurationSection(name);
        if (innerSection == null) {
          new Log().warn().t("Configuration section ").t(name).t(" missing, using default values.").send();
        } else {
          new ConfigSection(clazz, innerSection).load();
        }
      }
    }
    for (final Field field : this.sectionClass.getDeclaredFields()) {
      new ConfigField(field, this.section).load();;
    }
  }

  public void save() {

  }

}
