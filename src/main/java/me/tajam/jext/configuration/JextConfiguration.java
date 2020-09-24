package me.tajam.jext.configuration;

import java.util.HashMap;
import java.util.Map;

import me.tajam.jext.configuration.Configuration.ConfigFile;

@ConfigFile()
public class JextConfiguration extends Configuration {
  
  @ConfigSection()
  public static class Jext {
    
    @ConfigField()
    public static Boolean FORCE_RESOURCE_PACK = true;

    @ConfigField()
    public static String RESOURCE_PACK_DECLINE_KICK_MESSAGE = "Please enable resource pack for the music!";

    @ConfigField()
    public static Boolean IGNORE_FAILED_DOWNLOAD = false;

    @ConfigField()
    public static String FAILED_DOWNLOAD_KICK_MESSAGE = "Resource pack download failed, please re-join to try again.";

    @ConfigField()
    public static Boolean ALLOW_MUSIC_OVERLAPPING = false;

    @ConfigField()
    public static Map<String, DiscData> DISC = new HashMap<>();

  }

}
