package me.tajam.jext.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionMapHelper {

  private Object map;
  private Class<?> keyClass;
  private Class<?> valueClass;

  public ReflectionMapHelper(Field map, Object instance) {
    try {
      this.map = map.get(instance);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    final ParameterizedType type = (ParameterizedType) map.getGenericType();
    final Type keyType = type.getActualTypeArguments()[0];
    final Type valueType = type.getActualTypeArguments()[1];
    try {
      this.keyClass = Class.forName(keyType.getTypeName());
      this.valueClass = Class.forName(valueType.getTypeName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public boolean checkKeyClass(Class<?> clazz) {
    return this.keyClass.isAssignableFrom(clazz);
  }

  public boolean checkValueClass(Class<?> clazz) {
    return this.valueClass.isAssignableFrom(clazz);
  }

  public Class<?> getKeyClass() {
    return this.keyClass;
  }

  public Class<?> getValueClass() {
    return this.valueClass;
  }

  public void put(Object key, Object value) {
    if (!(checkKeyClass(key.getClass()) && checkValueClass(value.getClass()))) throw new IllegalArgumentException();
    try {
      final Method method = this.map.getClass().getDeclaredMethod("put", Object.class, Object.class);
      method.invoke(this.map, key, value);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
  
}
