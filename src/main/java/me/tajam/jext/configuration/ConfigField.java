package me.tajam.jext.configuration;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigAnnotation.MarkAsConfigField;

public class ConfigField {
  
  private Field field;
  private ConfigurationSection parentSection;

  public ConfigField(Field field, ConfigurationSection parentSection) {
    this.field = field;
    this.parentSection = parentSection;
  }

  public void load() {
    if (!field.isAnnotationPresent(MarkAsConfigField.class)) return;
    if (isMap(this.field)) {
      loadObject(this.parentSection, this.field, null);
    } else {
      loadValue();
    }
  }

  public void save() {

  }

  private void loadObject(ConfigurationSection section, Field field, Object instance) {
    final ReflectionConfigMap mapHelper = new ReflectionConfigMap(field, instance);
    if (mapHelper.isValid()) {
      final String mapName = field.getName().toLowerCase().replace('_', '-');
      final ConfigurationSection mapSection = section.getConfigurationSection(mapName);
      for (String key : mapSection.getKeys(false)) {
        try {
          final Object valueInstance = mapHelper.getValueClass().newInstance();
          final ConfigurationSection keySection = mapSection.getConfigurationSection(key);
          for (final Field valueField: mapHelper.getValueClass().getDeclaredFields()) {
            if (valueField.isAnnotationPresent(MarkAsConfigField.class)) {
              if (isMap(valueField)) {
                loadObject(keySection, valueField, valueInstance);
              } else {
                final String name = valueField.getName().toLowerCase().replace('_', '-');
                final Object o = keySection.get(name);
                if (o == null) {
                  new Log().warn().t("Section: ").t(key).t(", Field: ").t(name).t(" missing, using default values.").send();
                  continue;
                }
                valueField.set(valueInstance, o);
              }
            }
          }
          mapHelper.put(key, valueInstance);
        } catch (InstantiationException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void loadValue() {
    try {
      final String name = field.getName().toLowerCase().replace('_', '-');
      final Object object = this.parentSection.get(name);
      if (object == null) {
        new Log().warn().t("Configuration field ").t(name).t(" missing, using default values.").send();
        return;
      }
      this.field.set(null, object);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private boolean isMap(Field field) {
    return (Map.class.isAssignableFrom(field.getType()));
  }

}
