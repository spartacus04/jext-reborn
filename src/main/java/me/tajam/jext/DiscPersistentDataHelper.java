package me.tajam.jext;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.tajam.jext.namespace.JextNamespace;
import me.tajam.jext.namespace.JextNamespace.DefinedNamespace;

public class DiscPersistentDataHelper {

  private final String IDENTIFIER = "JEXT";

  private PersistentDataContainer container;
  private JextNamespace namespace;

  public DiscPersistentDataHelper(ItemMeta meta) {
    this.container = meta.getPersistentDataContainer();
    this.namespace = JextNamespace.getInstance();
  }

  public void setTitle(String value) {
    container.set(
      namespace.get(DefinedNamespace.TITLE),
      PersistentDataType.STRING,
      value
    );
  }

  public String getTitle() {
    return container.get(
      namespace.get(DefinedNamespace.TITLE),
      PersistentDataType.STRING
    );
  }

  public void setAuthor(String value) {
    container.set(
      namespace.get(DefinedNamespace.AUTHOR),
      PersistentDataType.STRING,
      value
    );
  }

  public String getAuthor() {
    return container.get(
      namespace.get(DefinedNamespace.AUTHOR),
      PersistentDataType.STRING
    );
  }

  public void setNamespaceID(String value) {
    container.set(
      namespace.get(DefinedNamespace.NAMESPACE_ID),
      PersistentDataType.STRING,
      value
    );
  }

  public String getNamespaceID() {
    return container.get(
      namespace.get(DefinedNamespace.NAMESPACE_ID),
      PersistentDataType.STRING
    );
  }

  public void setIdentifier() {
    container.set(
      namespace.get(DefinedNamespace.IDENTIFIER),
      PersistentDataType.STRING,
      IDENTIFIER
    );
  }

  public boolean checkIdentifier() {
    final String value = container.get(
      namespace.get(DefinedNamespace.IDENTIFIER),
      PersistentDataType.STRING
    );
    return value.equals(IDENTIFIER);
  }

}