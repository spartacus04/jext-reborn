package me.tajam.jext;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public enum JextNamespace {
  
  AUTHOR("jext.author"),
  NAMESPACE_ID("jext.namespace_id"),
  IDENTIFIER("jext.identifier"),
  TITLE("jext.title")

  ;

  public static void registerNamespace(JavaPlugin plugin) {
    for (JextNamespace namespace : JextNamespace.values()) {
      namespace.namespacedKey = new NamespacedKey(plugin, namespace.key);
    }
  }

  private String key;
  private NamespacedKey namespacedKey;

  private JextNamespace(String key) {
    this.key = key;
    this.namespacedKey = null;
  }
  
  public NamespacedKey get() {
    if (this.namespacedKey == null) throw new NullPointerException("Namespace not registered.");
    return this.namespacedKey;
  }

  public String getKey() {
    return this.key;
  }

}
