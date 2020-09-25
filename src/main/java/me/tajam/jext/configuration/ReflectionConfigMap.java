package me.tajam.jext.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.tajam.jext.Log;
import me.tajam.jext.configuration.ConfigAnnotation.MarkAsConfigObject;

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

  public boolean isValid() {
    return this.keyClass.isAssignableFrom(String.class) && 
      this.valueClass.isAnnotationPresent(MarkAsConfigObject.class);
  }

  public void put(Object key, Object value) {
    if (!(this.keyClass.isAssignableFrom(key.getClass()) && this.valueClass.isAssignableFrom(value.getClass()))) {
      new Log().eror().t("Error when loading an object in map: ").t(mapField.getName()).t(", ignoring this object.").send();
      return;
    }
    try {
      final Object map = mapField.get(this.instance);
      final Method method = map.getClass().getDeclaredMethod("put", Object.class, Object.class);
      method.invoke(map, key, value);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
  
}
