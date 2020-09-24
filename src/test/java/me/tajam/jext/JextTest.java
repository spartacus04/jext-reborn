package me.tajam.jext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class JextTest {

  public Map<String, Integer> map = new HashMap<>();

  @Test
  public void shouldAnswerWithTrue() {

    try {
      Object obj = getClass().getField("map").get(this);
      Method method = getClass().getField("map").get(this).getClass().getDeclaredMethod("put", Object.class, Object.class);
      method.invoke(obj, "test", 123);
      method.invoke(obj, "Casey", 2701);
    } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException
        | NoSuchFieldException | InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for (Entry<String, Integer> entry : map.entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue());
    }

  }
}
