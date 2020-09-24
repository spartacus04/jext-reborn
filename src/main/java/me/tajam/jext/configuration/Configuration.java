package me.tajam.jext.configuration;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE })
  public @interface ConfigFile {
    String value() default "config.yml";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.TYPE })
  public @interface ConfigSection {
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD })
  public @interface ConfigField {
  }

  // TODO: Need to somehow clean these up
  public void load(JavaPlugin plugin) {
    if (!this.getClass().isAnnotationPresent(ConfigFile.class))
      return;
    final ConfigFile annotation = this.getClass().getAnnotation(ConfigFile.class);
    final FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), annotation.value()));
    configuration.options().pathSeparator('§');
    loadSection(configuration, this.getClass());
  }

  private void loadSection(ConfigurationSection section, Class<?> clazz) {
    for (final Class<?> clazzz : clazz.getDeclaredClasses()) {
      if (clazzz.isAnnotationPresent(ConfigSection.class)) {
        loadSection(section.getConfigurationSection(clazzz.getSimpleName().toLowerCase()), clazzz);
      }
    }
    for (final Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(ConfigField.class)) {
        if (Map.class.isAssignableFrom(field.getType())) {
          final ReflectionMapHelper mapHelper = new ReflectionMapHelper(field, null);
          if (mapHelper.checkKeyClass(String.class) && mapHelper.getValueClass().isAnnotationPresent(ConfigSection.class)) {
            final ConfigurationSection mapSection = section.getConfigurationSection(field.getName().toLowerCase().replace('_', '-'));
            for (final String key : mapSection.getKeys(false)) {
              try {
                final Object obj = mapHelper.getValueClass().newInstance();
                final ConfigurationSection subSection = mapSection.getConfigurationSection(key);
                for (final Field objField: obj.getClass().getDeclaredFields()) {
                  if (objField.isAnnotationPresent(ConfigField.class)) {
                    final Object o = subSection.get(objField.getName().toLowerCase().replace('_', '-'));
                    if (o != null) objField.set(obj, o);
                  }
                }
                mapHelper.put(key, obj);
              } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
              }
            }
          }
          continue;
        }
        try {
          field.set(null, section.get(field.getName().toLowerCase().replace('_', '-')));
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
