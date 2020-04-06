package com.tajam.jext.disc;

import com.tajam.jext.config.ConfigData;
import com.tajam.jext.config.ConfigManager;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DiscPlayer {

  private static final double JUKEBOX_RANGE = 66.0;
  private static final float JUKEBOX_VOLUME = 3.0f;

  private Location location;
  private String namespace;

  public DiscPlayer(DiscContainer disc, Location location) {
    this(disc.getNamespace(), location);
  }

  public DiscPlayer(String namespace, Location location) {
    this.namespace = namespace;
    this.location = location;
  }
  
  public void setMusic(String namespace) {
    this.namespace = namespace;
  }

  public void setMusic(DiscContainer disc) {
    this.namespace = disc.getNamespace();
  }

  public void play() {
    World world = location.getWorld();
    ConfigManager manager = ConfigManager.getInstance();
    if (!manager.getBooleanData(ConfigData.BooleanData.Path.ALLOW_OVERLAP)) {
      stop();
    }
    world.playSound(location, namespace, JUKEBOX_VOLUME, 1.0f);
  }

  public void stop() {
    for (Player player : location.getWorld().getPlayers()) {
      if (player.getLocation().distance(location) < JUKEBOX_RANGE) {
        player.stopSound(namespace, SoundCategory.RECORDS);
      }
    }
  }

}