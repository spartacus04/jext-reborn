package me.tajam.jext;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.tajam.jext.namespace.JextNamespace;
import me.tajam.jext.namespace.JextNamespace.DefinedNamespace;

public class DiscPersistentDataHelper {
  
  private class PersistantDataString implements PersistentDataType<String, String> {

    @Override
    public Class<String> getPrimitiveType() {
      return String.class;
    }

    @Override
    public Class<String> getComplexType() {
      return String.class;
    }

    @Override
    public String toPrimitive(String complex, PersistentDataAdapterContext context) {
      return complex;
    }

    @Override
    public String fromPrimitive(String primitive, PersistentDataAdapterContext context) {
      return primitive;
    }

  }

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
      new PersistantDataString(),
      value
    );
  }

  public String getTitle() {
    return container.get(
      namespace.get(DefinedNamespace.TITLE),
      new PersistantDataString()
    );
  }

  public void setAuthor(String value) {
    container.set(
      namespace.get(DefinedNamespace.AUTHOR),
      new PersistantDataString(),
      value
    );
  }

  public String getAuthor() {
    return container.get(
      namespace.get(DefinedNamespace.AUTHOR),
      new PersistantDataString()
    );
  }

  public void setNamespaceID(String value) {
    container.set(
      namespace.get(DefinedNamespace.NAMESPACE_ID),
      new PersistantDataString(),
      value
    );
  }

  public String getNamespaceID() {
    return container.get(
      namespace.get(DefinedNamespace.NAMESPACE_ID),
      new PersistantDataString()
    );
  }

  public void setIdentifier() {
    container.set(
      namespace.get(DefinedNamespace.IDENTIFIER),
      new PersistantDataString(),
      IDENTIFIER
    );
  }

  public boolean checkIdentifier() {
    final String value = container.get(
      namespace.get(DefinedNamespace.IDENTIFIER),
      new PersistantDataString()
    );
    return value.equals(IDENTIFIER);
  }

}