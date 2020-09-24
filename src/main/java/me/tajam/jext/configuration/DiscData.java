package me.tajam.jext.configuration;

import java.util.ArrayList;
import java.util.List;

import me.tajam.jext.configuration.Configuration.ConfigField;
import me.tajam.jext.configuration.Configuration.ConfigSection;

@ConfigSection()
public class DiscData {

  @ConfigField()
  public String NAMESPACE = "Unnamed Disc";

  @ConfigField()
  public String AUTHOR = "Annonymous";

  @ConfigField()
  public int MODEL_DATA = 0;

  @ConfigField()
  public boolean CREEPER_DROP = true;

  @ConfigField()
  public List<String> LORE = new ArrayList<>();

}
