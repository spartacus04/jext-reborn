package me.spartacus04.jext.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import me.spartacus04.jext.Log;
import me.spartacus04.jext.configuration.ConfigUtil.MarkAsConfigField;
import me.spartacus04.jext.configuration.ConfigUtil.MarkAsConfigObject;

public class ReflectionConfigMap {

  private Field mapField;
  private Object instance;
  private Class<?> keyClass;
  private Class<?> valueClass;

  public ReflectionConfigMap(Field mapField, Object instance) {
    this.mapField = mapField;
    this.instance = instance;
    final ParameterizedType type = (ParameterizedType) mapField.getGenericType();
    final Type keyType = type.getActualTypeArguments()[0];
    final Type valueType = type.getActualTypeArguments()[1];
    try {
      this.keyClass = Class.forName(keyType.getTypeName());
      this.valueClass = Class.forName(valueType.getTypeName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public Class<?> getKeyClass() {
    return this.keyClass;
  }

  public Class<?> getValueClass() {
    return this.valueClass;
  }

  public List<Field> getValueFields() {
    final List<Field> fields = new ArrayList<>();
    for (Field field : this.valueClass.getDeclaredFields()) {
      if (field.isAnnotationPresent(MarkAsConfigField.class)) {
        fields.add(field);
      }
    }
    return fields;
  }

  public Object instantiateValueObject() {
    try {
      return this.valueClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean isValid() {
    return this.keyClass.isAssignableFrom(String.class)
        && this.valueClass.isAnnotationPresent(MarkAsConfigObject.class);
  }

  public void put(Object key, Object value) {
    if (!(this.keyClass.isAssignableFrom(key.getClass()) && this.valueClass.isAssignableFrom(value.getClass()))) {
      new Log().eror().t("Error when loading an object in map: ").t(mapField.getName()).t(", ignoring this object.")
          .send();
      return;
    }
    try {
      final Object map = getMapInstance();
      final Method method = map.getClass().getDeclaredMethod("put", Object.class, Object.class);
      method.invoke(map, key, value);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public void clear() {
    try {
      final Object map = getMapInstance();
      final Method method = map.getClass().getDeclaredMethod("clear");
      method.invoke(map);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Set<Entry<String, Object>> entries() {
    try {
      final Object map = getMapInstance();
      final Map<String, Object> m = new HashMap<>((Map)map);
      return m.entrySet();
    } catch (SecurityException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Object getMapInstance() {
    try {
      Object map = mapField.get(this.instance);
      if (map == null) {
        map = mapField.getType().newInstance();
        mapField.set(this.instance, map);
      }
      return map;
    } catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
      return null;
    }
  }
  
}
