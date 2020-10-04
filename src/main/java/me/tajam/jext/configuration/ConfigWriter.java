package me.tajam.jext.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.tajam.jext.configuration.ConfigUtil.MarkAsConfigFile;
import me.tajam.jext.configuration.ConfigUtil.PlaceComment;

public class ConfigWriter {

  private FileWriter writer;

  public ConfigWriter(File file) {
    try {
      this.writer = new FileWriter(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeComment(AnnotatedElement element, int level) {
    if (element.isAnnotationPresent(PlaceComment.class)) {
      final PlaceComment annotation = element.getAnnotation(PlaceComment.class);
      writeIdentation(level);
      for (final String comment : annotation.value()) {
        try {
          this.writer.append("# ").append(comment).append('\n');
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void writeHeader(Class<?> clazz) {
    if (clazz.isAnnotationPresent(MarkAsConfigFile.class)) {
      final MarkAsConfigFile annotation = clazz.getAnnotation(MarkAsConfigFile.class);
      try {
        this.writer.append("# Please don't not touch this!\n").append("version: ").append(annotation.versionString())
            .append("\n\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void writeField(Field field, int level, Object instance) {
    writeIdentation(level);
    try {
      this.writer.append(ConfigUtil.javaNametoYml(field.getName())).append(':');
      final Object object = field.get(instance);
      if (Map.class.isAssignableFrom(object.getClass())) return;
      if (List.class.isAssignableFrom(object.getClass())) {
        writeList(new ArrayList<>((List)object), level);
      } else {
        this.writer.append(' ').append(object.toString());
      }
    } catch (IllegalArgumentException | IllegalAccessException | IOException e) {
      e.printStackTrace();
    }
  }

  public void writeSection(Class<?> clazz, int level) {
    writeIdentation(level);
    try {
      this.writer.append(ConfigUtil.javaNametoYml(clazz.getSimpleName())).append(": \n\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeList(List<Object> objects, int level) {
    for (Object object : objects) {
      writeIdentation(level);
      try {
        this.writer.append("  - ").append(object.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void writeIdentation(int level) {
    final int value = (level - 1) * 2;
    for(int i = 0; i < value; i++) {
      try {
        this.writer.append(' ');
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
