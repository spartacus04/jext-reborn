package me.tajam.jext.disc;

import me.tajam.jext.config.ConfigData;
import me.tajam.jext.config.ConfigManager;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DiscPlayer {

  private static final double JUKEBOX_RANGE = 66.0;
  private static final float JUKEBOX_VOLUME = 3.0f;

  private Location location;
  private String namespace;
  private float volume;
  private float pitch;

  public DiscPlayer(DiscContainer disc, Location location) {
    this(disc.getNamespace(), location);
  }

  public DiscPlayer(String namespace, Location location) {
    this.namespace = namespace;
    this.location = location;
    this.volume = JUKEBOX_VOLUME;
    this.pitch = 1.0f;
  }
  
  public DiscPlayer setMusic(String namespace) {
    this.namespace = namespace;
    return this;
  }

  public DiscPlayer setMusic(DiscContainer disc) {
    this.namespace = disc.getNamespace();
    return this;
  }

  public DiscPlayer setVolume(float value) {
    this.volume = value;
    return this;
  }

  public DiscPlayer setPitch(float value) {
    this.pitch = value;
    return this;
  }

  public void play() {
    World world = location.getWorld();
    ConfigManager manager = ConfigManager.getInstance();
    if (!manager.getBooleanData(ConfigData.BooleanData.Path.ALLOW_OVERLAP)) {
      stop();
    }
    world.playSound(location, namespace, SoundCategory.RECORDS, volume, pitch);
  }

  public void stop() {
    for (Player player : location.getWorld().getPlayers()) {
      if (player.getLocation().distance(location) <= JUKEBOX_RANGE) {
        player.stopSound(namespace, SoundCategory.RECORDS);
      }
    }
  }

}