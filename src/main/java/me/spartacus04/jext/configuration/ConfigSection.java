package me.spartacus04.jext.configuration;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.spartacus04.jext.Log;
import me.spartacus04.jext.configuration.ConfigUtil.MarkAsConfigField;
import me.spartacus04.jext.configuration.ConfigUtil.MarkAsConfigSection;

public class ConfigSection extends Configuration {

  private Class<?> clazz;

  public ConfigSection(Class<?> clazz, ConfigurationSection section) {
    this(clazz, section, null);
  }

  public ConfigSection(Object instance, ConfigurationSection section) {
    this(instance.getClass(), section, instance);
  }

  private ConfigSection(Class<?> clazz, ConfigurationSection section, Object instance) {
    this.clazz = clazz;
    for (final Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(MarkAsConfigField.class)) {
        final boolean isMap = (Map.class.isAssignableFrom(field.getType()));
        final Configuration child = isMap ? new ConfigFieldObject(field, section, instance) : new ConfigField(field, section, instance);
        addChild(child);
      }
    }
    for (final Class<?> clz : clazz.getDeclaredClasses()) {
      if (clz.isAnnotationPresent(MarkAsConfigSection.class)) {
        final String name = ConfigUtil.javaNametoYml(clz.getSimpleName());
        ConfigurationSection innerSection = section.getConfigurationSection(name);
        if (innerSection == null) {
          new Log().warn().t("Configuration section ").t(name).t(" missing, using default values.").send();
          innerSection = section.createSection(name);
        }
        final Configuration child = new ConfigSection(clz, innerSection);
        addChild(child);
      }
    }
  }

  @Override
  public void load() {
    final Iterator<Configurable> iter = getChildsIterable();
    while(iter.hasNext()) {
      iter.next().load();
    }
  }

  @Override
  public void save(ConfigWriter writer) {
    final int level = getLevel();
    if (level > 0) {
      writer.writeComment(this.clazz, level);
      writer.writeSection(this.clazz, level);
    }
    final Iterator<Configurable> iter = getChildsIterable();
    while(iter.hasNext()) {
      iter.next().save(writer);
    }
  }

}
