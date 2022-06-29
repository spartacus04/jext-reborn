package me.spartacus04.jext;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotVersion {
  
  private static SpigotVersion instance;

  private String version;

  public SpigotVersion(JavaPlugin plugin) {
    this.version = plugin.getServer().getBukkitVersion().substring(0, 4);
    SpigotVersion.instance = this;
  }

  public static boolean isVersion1_16() {
    return instance.version.equals("1.16");
  }

  public static boolean isVersion1_15() {
    return instance.version.equals("1.15");
  }

  public static boolean isVersion1_14() {
    return instance.version.equals("1.14");
  }

  public static boolean isVersion1_13() {
    return instance.version.equals("1.13");
  }

}