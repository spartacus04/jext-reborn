package me.tajam.jext.disc;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.tajam.jext.JextNamespace;

public class DiscPersistentDataContainer {

  private final String IDENTIFIER = "JEXT";

  private PersistentDataContainer container;

  public DiscPersistentDataContainer(ItemMeta meta) {
    this.container = meta.getPersistentDataContainer();
  }

  public void setTitle(String value) {
    container.set(
      JextNamespace.TITLE.get(),
      PersistentDataType.STRING,
      value
    );
  }

  public String getTitle() {
    return container.get(
      JextNamespace.TITLE.get(),
      PersistentDataType.STRING
    );
  }

  public void setAuthor(String value) {
    container.set(
      JextNamespace.AUTHOR.get(),
      PersistentDataType.STRING,
      value
    );
  }

  public String getAuthor() {
    return container.get(
      JextNamespace.AUTHOR.get(),
      PersistentDataType.STRING
    );
  }

  public void setNamespaceID(String value) {
    container.set(
      JextNamespace.NAMESPACE_ID.get(),
      PersistentDataType.STRING,
      value
    );
  }

  public String getNamespaceID() {
    return container.get(
      JextNamespace.NAMESPACE_ID.get(),
      PersistentDataType.STRING
    );
  }

  public void setIdentifier() {
    container.set(
      JextNamespace.IDENTIFIER.get(),
      PersistentDataType.STRING,
      IDENTIFIER
    );
  }

  public boolean checkIdentifier() {
    final String value = container.get(
      JextNamespace.IDENTIFIER.get(),
      PersistentDataType.STRING
    );
    return value.equals(IDENTIFIER);
  }

}