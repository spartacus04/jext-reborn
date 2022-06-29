package me.spartacus04.jext.configuration;

public interface Configurable {
  
  public void save(ConfigWriter writer);

  public void load();

}
