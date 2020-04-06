package com.tajam.jext.config.field;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigFieldListString implements ConfigFieldList<String> {

  private String path;
  private ArrayList<String> data;

  public ConfigFieldListString(String path, ArrayList<String> defaultData) {
    this.path = path;
    this.data = defaultData;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public ArrayList<String> getData() {
    return data;
  }

  @Override
  public boolean updateData(ConfigurationSection section) {
    if (!section.contains(path)) {
      section.set(path, data);
      return true;
    }
    this.data = new ArrayList<String>(section.getStringList(path));
    return false;
  }

}