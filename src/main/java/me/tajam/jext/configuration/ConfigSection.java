package me.tajam.jext.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigUtil.MarkAsConfigField;
import me.tajam.jext.configuration.ConfigUtil.MarkAsConfigSection;

public class ConfigSection implements SaveLoadable {

  private List<SaveLoadable> childs;

  public ConfigSection(Class<?> clazz, ConfigurationSection section) {
    this.childs = new ArrayList<>();
    for (final Class<?> clz : clazz.getDeclaredClasses()) {
      if (clz.isAnnotationPresent(MarkAsConfigSection.class)) {
        final String name = ConfigUtil.javaNametoYml(clz.getSimpleName());
        final ConfigurationSection innerSection = section.getConfigurationSection(name);
        if (innerSection == null) {
          new Log().warn().t("Configuration section ").t(name).t(" missing, using default values.").send();
          continue;
        }
        childs.add(new ConfigSection(clz, innerSection));
      }
    }
    for (final Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(MarkAsConfigField.class)) {
        if (Map.class.isAssignableFrom(field.getType())) {
          childs.add(new ConfigFieldObject(field, section));
        } else {
          childs.add(new ConfigField(field, section));
        }
      }
    }
  }

  @Override
  public void load() {
    for (final SaveLoadable saveLoadable : childs) {
      saveLoadable.load();
    }
  }

  @Override
  public void save() {
    for (final SaveLoadable saveLoadable : childs) {
      saveLoadable.save();
    }
  }

}
