package me.tajam.jext.config.field;

import me.tajam.jext.SMS;

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

  public void updateData(ConfigurationSection section, Class<T> clazz) {
    if (!section.isSet(path)) {
      new SMS().warn().t("\"").o(path).t("\" missing in configuration file, will set to default value.").send();
    }
    this.data = section.getObject(path, clazz);
  }

}