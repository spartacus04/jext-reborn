package me.tajam.jext.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tajam.jext.configuration.ConfigUtil.*;

@MarkAsConfigFile(versionString = "1.2.2")
public final class ConfigYmlv1_2_2 {

  @MarkAsConfigSection
  public static class RESOURCE_PACK {
    @PlaceComment("Kick players who decline server resource pack")
    @MarkAsConfigField public static Boolean FORCE_RESOURCE_PACK = true;
    @PlaceComment("Ignore if player somehow failed downloading the resource pack")
    @MarkAsConfigField public static Boolean IGNORE_FAILED_DOWNLOAD = false;
    @PlaceComment("Kick message for rejecting server resource pack")
    @MarkAsConfigField public static String RESOURCE_PACK_DECLINE_KICK_MESSAGE = "Please enable resource pack for the music!";
    @PlaceComment("Kick message for player who failed downloading the resource pack")
    @MarkAsConfigField public static String FAILED_DOWNLOAD_KICK_MESSAGE = "Resource pack download failed, please re-join to try again.";
  }
  
  public static class SETTINGS {
    @PlaceComment("Allow/Disallow overlapping music (two jukebox playing same music together)")
    @MarkAsConfigField public static Boolean ALLOW_MUSIC_OVERLAPPING = false;
    @PlaceComment("Disallow parrot dancing to the custom music")
    @MarkAsConfigField public static Boolean ALLOW_PARROT_DANCE = true;
    @PlaceComment({
      "The delay in ticks to override original music (Hard minimum value is 1)",
      "Ignore this if parrot is not allowed to dance"
    })
    @MarkAsConfigField public static Integer PACKET_DELAY_TICKS = 4;
  }
  
  @PlaceComment("Register your discs here")
  @MarkAsConfigField public static Map<String, DiscData> DISC = new HashMap<>();

  static {
    DiscData disc_0 = new DiscData();
    disc_0.AUTHOR = "C148";
    disc_0.CREEPER_DROP = true;
    disc_0.LORE = Arrays.asList("Minecraft originals");
    disc_0.MODEL_DATA = 0;
    disc_0.TITLE = "Cat";

    DiscData disc_1 = new DiscData();
    disc_1.AUTHOR = "C148";
    disc_1.CREEPER_DROP = true;
    disc_1.LORE = Arrays.asList("Minecraft originals");
    disc_1.MODEL_DATA = 0;
    disc_1.TITLE = "Stal";

    DISC.put("music_disc.cat", disc_0);
    DISC.put("music_disc.stal", disc_1);
  }

  @MarkAsConfigObject
  public static class DiscData {

    @MarkAsConfigField public String TITLE = "Untitled";
    @MarkAsConfigField public String AUTHOR = "Annonymous";
    @MarkAsConfigField public int MODEL_DATA = 0;
    @MarkAsConfigField public boolean CREEPER_DROP = true;
    @MarkAsConfigField public List<String> LORE = new ArrayList<>();

  }

}
