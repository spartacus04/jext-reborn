package me.spartacus04.jext.config.field;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

import me.spartacus04.jext.Log;

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
      new Log().warn().t("\"").o(path).t("\" missing in configuration file, will set to default value.").send();
      return;
    }
    this.data = new ArrayList<String>(section.getStringList(path));
  }

}