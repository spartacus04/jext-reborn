package me.tajam.jext.config.field;

import java.util.ArrayList;

import me.tajam.jext.Logger;

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
  public void updateData(ConfigurationSection section) {
    if (!section.isSet(path)) {
      Logger.warning("\"" + path + "\" missing in configuration file, will set to default value.");
    }
    this.data = new ArrayList<String>(section.getStringList(path));
  }

}