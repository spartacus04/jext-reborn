package com.tajam.jext.config.field;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigField<T> {

  private String path;
  private T data;

  public ConfigField(String path, T defaultData) {
    this.path = path;
    this.data = defaultData;
  }

  public String getPath() {
    return this.path;
  }

  public T getData() {
    return this.data;
  }

  public boolean updateData(ConfigurationSection section, Class<T> clazz) {
    if (!section.contains(path)) {
      section.set(path, data);
      return true;
    }
    this.data = section.getObject(path, clazz);
    return false;
  }

}