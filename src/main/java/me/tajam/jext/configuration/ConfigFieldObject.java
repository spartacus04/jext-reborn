package me.tajam.jext.configuration;

import java.lang.reflect.Field;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import me.tajam.jext.Log;

public class ConfigFieldObject extends ConfigField {

  private ReflectionConfigMap configMap;
  private ConfigurationSection section;
  private String fieldName;

  public ConfigFieldObject(Field field, ConfigurationSection section, Object instance) {
    super(field, section, instance);
    this.configMap = new ReflectionConfigMap(field, instance);
    this.fieldName = ConfigUtil.javaNametoYml(this.field.getName());
    this.section = section;
  }

  public ConfigFieldObject(Field field, ConfigurationSection section) {
    this(field, section, null);
  }

  @Override
  public void load() {
    this.configMap.clear();
    final ConfigurationSection mapSection = this.section.getConfigurationSection(this.fieldName);
    if (mapSection == null) {
      new Log().warn().t("Configuration field ").t(this.fieldName).t(" missing, using default values.").send();
      return;
    }
    for (final String key : mapSection.getKeys(false)) {
      final ConfigurationSection keySection = mapSection.getConfigurationSection(key);
      final Object valueInstance = this.configMap.instantiateValueObject();
      final Configuration configuration = new ConfigSection(valueInstance, keySection);
      addChild(configuration);
      configuration.load();
      this.configMap.put(key, valueInstance);
    }
  }

  @Override
  public void save(ConfigWriter writer) {
    writer.writeComment(this.field, getLevel());
    writer.writeField(this.field, getLevel(), this.instance);
    ConfigurationSection mapSection = this.section.getConfigurationSection(this.fieldName);
    if (mapSection == null) mapSection = this.section.createSection(this.fieldName);
    for (final Entry<String, Object> entry : this.configMap.entries()) {
      final String key = entry.getKey();
      ConfigurationSection keySection = mapSection.getConfigurationSection(key);
      if (keySection == null) keySection = mapSection.createSection(key);
      final Configuration configuration = new ConfigSection(entry.getValue(), keySection);
      addChild(configuration);
      configuration.save(writer);
    } 
  }

}
