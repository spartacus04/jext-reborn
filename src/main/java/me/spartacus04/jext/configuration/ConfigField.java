package me.spartacus04.jext.configuration;

import java.lang.reflect.Field;

import org.bukkit.configuration.ConfigurationSection;

import me.spartacus04.jext.Log;

public class ConfigField extends Configuration {
  
  protected Field field;
  protected ConfigurationSection section;
  protected Object instance;

  public ConfigField(Field field, ConfigurationSection section, Object instance) {
    this.field = field;
    this.section = section;
    this.instance = instance;
  }

  public ConfigField(Field field, ConfigurationSection section) {
    this(field, section, null);
  }

  @Override
  public void load() {
    final String name = ConfigUtil.javaNametoYml(this.field.getName());
    final Object object = this.section.get(name);
    if (object == null) {
      new Log().warn().t("Configuration field ").t(name).t(" missing, using default values.").send();
      return;
    }
    try {
      this.field.set(this.instance, object);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void save(ConfigWriter writer) {
    writer.writeComment(this.field, getLevel());
    writer.writeField(this.field, getLevel(), this.instance);
  }

}
