package me.spartacus04.jext.disc;

import me.spartacus04.jext.config.ConfigData;
import me.spartacus04.jext.config.ConfigManager;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DiscPlayer {

  private static final double JUKEBOX_RANGE_MULTIPLY = 16.0;
  private static final float JUKEBOX_VOLUME = 4.0f;

  private String namespace;
  private float volume;
  private float pitch;

  public DiscPlayer(DiscContainer disc) {
    this(disc.getNamespace());
  }

  public DiscPlayer(String namespace) {
    this.namespace = namespace;
    this.volume = JUKEBOX_VOLUME;
    this.pitch = 1.0f;
  }

  public DiscPlayer setVolume(float value) {
    this.volume = value;
    return this;
  }

  public DiscPlayer setPitch(float value) {
    this.pitch = value;
    return this;
  }

  public void play(Location location) {
    World world = location.getWorld();
    ConfigManager manager = ConfigManager.getInstance();
    if (!manager.getBooleanData(ConfigData.BooleanData.Path.ALLOW_OVERLAP)) {
      stop(location);
    }
    world.playSound(location, namespace, SoundCategory.RECORDS, volume, pitch);
  }

  public void stop(Location location) {
    for (Player player : location.getWorld().getPlayers()) {
      if (player.getLocation().distance(location) <= JUKEBOX_VOLUME * JUKEBOX_RANGE_MULTIPLY) {
        player.stopSound(namespace, SoundCategory.RECORDS);
      }
    }
  }
}