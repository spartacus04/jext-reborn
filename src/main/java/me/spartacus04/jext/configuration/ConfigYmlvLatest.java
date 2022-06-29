package me.spartacus04.jext.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigUtil.MarkAsConfigFile(versionString = "1.2.2")
public final class ConfigYmlvLatest {

  @ConfigUtil.MarkAsConfigSection
  public static class RESOURCE_PACK {
    @ConfigUtil.PlaceComment("Kick players who decline server resource pack")
    @ConfigUtil.MarkAsConfigField
    public static Boolean FORCE_RESOURCE_PACK = true;
    @ConfigUtil.PlaceComment("Ignore if player somehow failed downloading the resource pack")
    @ConfigUtil.MarkAsConfigField
    public static Boolean IGNORE_FAILED_DOWNLOAD = false;
    @ConfigUtil.PlaceComment("Kick message for rejecting server resource pack")
    @ConfigUtil.MarkAsConfigField
    public static String RESOURCE_PACK_DECLINE_KICK_MESSAGE = "Please enable resource pack for the music!";
    @ConfigUtil.PlaceComment("Kick message for player who failed downloading the resource pack")
    @ConfigUtil.MarkAsConfigField
    public static String FAILED_DOWNLOAD_KICK_MESSAGE = "Resource pack download failed, please re-join to try again.";
  }
  
  public static class SETTINGS {
    @ConfigUtil.PlaceComment("Allow/Disallow overlapping music (two jukebox playing same music together)")
    @ConfigUtil.MarkAsConfigField
    public static Boolean ALLOW_MUSIC_OVERLAPPING = false;
    /*
    @PlaceComment("Disallow parrot dancing to the custom music")
    @MarkAsConfigField public static Boolean ALLOW_PARROT_DANCE = true;
    @PlaceComment({
      "Don't touch this if you don't know what's going on!",
      "The delay in ticks to override original music (Hard minimum value is 1)",
      "Ignore this if parrot is not allowed to dance"
    })
    @MarkAsConfigField public static Integer PACKET_DELAY_TICKS = 4;
    */
  }
  
  @ConfigUtil.PlaceComment("Register your discs here")
  @ConfigUtil.MarkAsConfigField
  public static Map<String, DiscData> DISC = new HashMap<>();

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

  @ConfigUtil.MarkAsConfigObject
  public static class DiscData {

    @ConfigUtil.MarkAsConfigField
    public String TITLE = "Untitled";
    @ConfigUtil.MarkAsConfigField
    public String AUTHOR = "Annonymous";
    @ConfigUtil.MarkAsConfigField
    public int MODEL_DATA = 0;
    @ConfigUtil.MarkAsConfigField
    public boolean CREEPER_DROP = true;
    @ConfigUtil.MarkAsConfigField
    public List<String> LORE = new ArrayList<>();

  }

}
