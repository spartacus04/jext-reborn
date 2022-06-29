package me.spartacus04.jext.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ConfigUtil {
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE, ElementType.FIELD })
  public @interface PlaceComment {
    String[] value();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE })
  public @interface MarkAsConfigFile {
    String value() default "config.yml";
    String versionString();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE })
  public @interface MarkAsConfigSection {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE })
  public @interface MarkAsConfigObject {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD })
  public @interface MarkAsConfigField {
  }

  public static String javaNametoYml(String name) {
    return name.toLowerCase().replace('_', '-');
  }

  public static String ymlNametoJava(String name) {
    return name.toUpperCase().replace('-', '_');
  }

}
