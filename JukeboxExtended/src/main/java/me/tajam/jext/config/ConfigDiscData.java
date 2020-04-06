package me.tajam.jext.config;

import java.util.ArrayList;
import java.util.HashMap;

import me.tajam.jext.config.field.ConfigField;
import me.tajam.jext.config.field.ConfigFieldListString;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigDiscData {

  public enum Path {

    NAMESPACE,
    AUTHOR,
    MODEL_DATA,
    LORES

  }

  private String name;
  private HashMap<Path, ConfigField<String>> stringMap;
  private HashMap<Path, ConfigField<Integer>> integerMap;
  private ConfigFieldListString lores;

  public ConfigDiscData(String name) {

    this.name = name;

    stringMap = new HashMap<>();
    stringMap.put(Path.NAMESPACE, new ConfigField<String>("namespace", "music_disc.cat"));
    stringMap.put(Path.AUTHOR, new ConfigField<String>("author", "C148"));

    integerMap = new HashMap<>();
    integerMap.put(Path.MODEL_DATA, new ConfigField<Integer>("model-data", 0));

    lores = new ConfigFieldListString("lore", new ArrayList<String>());
    
  }

  public void load(ConfigurationSection section) {
    ConfigurationSection subsection = section.getConfigurationSection(name);
    for (Path key : stringMap.keySet()) {
      ConfigField<String> field = stringMap.get(key);
      field.updateData(subsection, String.class);
    }
    for (Path key : integerMap.keySet()) {
      ConfigField<Integer> field = integerMap.get(key);
      field.updateData(subsection, Integer.class);
    }
    lores.updateData(subsection);
  }

  public String getName() {
    return this.name;
  }

  public String getStringData(Path key) {
    return stringMap.get(key).getData();
  }

  public Integer getIntegerData(Path key) {
    return integerMap.get(key).getData();
  }

  public ArrayList<String> getLores() {
    return new ArrayList<String>(this.lores.getData());
  }

}