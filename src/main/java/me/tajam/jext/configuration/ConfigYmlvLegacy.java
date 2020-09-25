package me.tajam.jext.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tajam.jext.configuration.ConfigAnnotation.*;

@MarkAsConfigFile(versionString = "legacy")
public final class ConfigYmlvLegacy {
  
  @MarkAsConfigSection
  public static class Jext {
    
    @MarkAsConfigField public static boolean FORCE_RESOURCE_PACK = true;
    @MarkAsConfigField public static String RESOURCE_PACK_DECLINE_KICK_MESSAGE = "Please enable resource pack for the music!";
    @MarkAsConfigField public static boolean IGNORE_FAILED_DOWNLOAD = false;
    @MarkAsConfigField public static String FAILED_DOWNLOAD_KICK_MESSAGE = "Resource pack download failed, please re-join to try again.";
    @MarkAsConfigField public static boolean ALLOW_MUSIC_OVERLAPPING = false;
    @MarkAsConfigField public static Map<String, DiscData> DISC = new HashMap<>();

  }

  @MarkAsConfigObject
  public static class DiscData {

    @MarkAsConfigField public String NAMESPACE = "Unnamed Disc";
    @MarkAsConfigField public String AUTHOR = "Annonymous";
    @MarkAsConfigField public int MODEL_DATA = 0;
    @MarkAsConfigField public boolean CREEPER_DROP = true;
    @MarkAsConfigField public List<String> LORE = new ArrayList<>();

  }

}
