package com.tajam.jext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class DiscBuffer {

  HashMap<String, Disc> discMap;

  public DiscBuffer() {
    discMap = new HashMap<String, Disc>();
  }

  public void loadDiscs(FileConfiguration file) {
    ConfigurationSection section = file.getConfigurationSection("disc");
    final Set<String> keys = section.getKeys(false);
    for (String key : keys) {
      ConfigurationSection subsection = section.getConfigurationSection(key);
      final String title = key;
      final String author = subsection.getString("author");
      final String nsid = subsection.getString("audio-file");
      final int modelId = subsection.getInt("model-id");
      final ArrayList<String> lores = new ArrayList<>(subsection.getStringList("lore"));
      discMap.put(nsid, new Disc(title, author, nsid, modelId, lores));
    }
  }

  public Disc getDisc(String nsid) {
    return discMap.get(nsid);
  }

}