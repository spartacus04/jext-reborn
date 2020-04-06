package com.tajam.jext.config;

import java.util.HashMap;

import com.tajam.jext.config.field.ConfigField;

public final class ConfigData {

  public static final String PATH = "jext";

  public static class BooleanData {

    public enum Path {

      FORCE_PACK,
      IGNORE_FAIL,
      ALLOW_OVERLAP

    }

    public static HashMap<Path, ConfigField<Boolean>> DataMap;

    static {
      HashMap<Path, ConfigField<Boolean>> map = new HashMap<>();
      map.put(Path.FORCE_PACK, new ConfigField<Boolean>("force-resource-pack", true));
      map.put(Path.IGNORE_FAIL, new ConfigField<Boolean>("ignore-failed-download", false));
      map.put(Path.ALLOW_OVERLAP, new ConfigField<Boolean>("allow-music-overlapping", false));
      DataMap = map;
    }

  }

  public static class StringData {

    public enum Path {
      
      KICK_DECLINE_MESSAGE,
      KICK_FAIL_MESSAGE

    }

    public static HashMap<Path, ConfigField<String>> DataMap;

    static {
      HashMap<Path, ConfigField<String>> map = new HashMap<>();
      map.put(Path.KICK_DECLINE_MESSAGE, new ConfigField<String>("resource-pack-decline-kick-message", "Unset kick message"));
      map.put(Path.KICK_FAIL_MESSAGE, new ConfigField<String>("failed-download-kick-message", "Unset kick message"));
      DataMap = map;
    }

  }

}