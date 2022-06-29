package me.spartacus04.jext.config.field;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigFieldList<T> {

  public String getPath();
  public ArrayList<T> getData();
  public void updateData(ConfigurationSection section);

}