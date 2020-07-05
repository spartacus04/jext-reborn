package me.tajam.jext.namespace;

import java.util.HashMap;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class JextNamespace {

  private static JextNamespace instance = null;
  public static JextNamespace getInstance() {
    if (instance == null) {
      instance = new JextNamespace();
    }
    return instance;
  }

  public enum DefinedNamespace {
    AUTHOR("jext.author"),
    NAMESPACE_ID("jext.namespace_id"),
    IDENTIFIER("jext.identifier"),
    TITLE("jext.title")
    ;

    private String value;

    private DefinedNamespace(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }

  }

  private HashMap<DefinedNamespace, NamespacedKey> namespaceMap;

  private JextNamespace() {
    namespaceMap = new HashMap<>();
  }

  public void registerNamespace(JavaPlugin plugin) {
    new NamespaceAuthor(plugin, namespaceMap);
    new NamespaceDisc(plugin, namespaceMap);
    new NamespaceIdentifier(plugin, namespaceMap);
    new NamespaceTitle(plugin, namespaceMap);
  }

  public NamespacedKey get(DefinedNamespace key) {
    return this.namespaceMap.get(key);
  }

}