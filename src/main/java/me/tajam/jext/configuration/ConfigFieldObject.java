package me.tajam.jext.configuration;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigUtil.MarkAsConfigField;

public class ConfigFieldObject extends ConfigField {

  private ReflectionConfigMap configMap;

  public ConfigFieldObject(Field field, ConfigurationSection section, Object instance) {
    super(field, section, instance);
    this.configMap = new ReflectionConfigMap(field, instance);
    this.configMap.clear();
  }

  public ConfigFieldObject(Field field, ConfigurationSection section) {
    this(field, section, null);
  }

  @Override
  public void load() {
    final String name = ConfigUtil.javaNametoYml(this.field.getName());
    final ConfigurationSection mapSection = this.section.getConfigurationSection(name);
    if (mapSection == null) {
      new Log().warn().t("Configuration field ").t(name).t(" missing, using default values.").send();
      return;
    }
    for (final String key : mapSection.getKeys(false)) {
      final ConfigurationSection keySection = mapSection.getConfigurationSection(key);
      final Object valueInstance = this.configMap.instantiateValueObject();
      for (final Field valueField : this.configMap.getValueFields()) {
        if (!valueField.isAnnotationPresent(MarkAsConfigField.class)) continue;
        if (Map.class.isAssignableFrom(valueField.getType())) {
          new ConfigFieldObject(valueField, keySection, valueInstance).load();
          continue;
        }
        new ConfigField(valueField, keySection, valueInstance).load();
      }
      this.configMap.put(key, valueInstance);
    }
  }

  @Override
  public void save() {

  }

}
