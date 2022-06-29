package me.spartacus04.jext.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.spartacus04.jext.configuration.ConfigUtil.*;

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

    static {
      DiscData disc_0 = new DiscData();
      disc_0.AUTHOR = "C148";
      disc_0.CREEPER_DROP = true;
      disc_0.LORE = Arrays.asList("Minecraft originals");
      disc_0.MODEL_DATA = 0;
      disc_0.NAMESPACE= "music_disc.cat";
  
      DiscData disc_1 = new DiscData();
      disc_1.AUTHOR = "C148";
      disc_1.CREEPER_DROP = true;
      disc_1.LORE = Arrays.asList("Minecraft originals");
      disc_1.MODEL_DATA = 0;
      disc_1.NAMESPACE = "music_disc.stal";
  
      DISC.put("Cat", disc_0);
      DISC.put("Stal", disc_1);
    }

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
