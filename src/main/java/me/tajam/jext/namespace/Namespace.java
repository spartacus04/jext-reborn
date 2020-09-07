package me.tajam.jext.namespace;

import java.util.HashMap;

import me.tajam.jext.namespace.JextNamespace.DefinedNamespace;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

abstract class Namespace {

  Namespace (JavaPlugin plugin, HashMap<DefinedNamespace, NamespacedKey> namespaceMap) {
    final NamespacedKey namespacedKey = new NamespacedKey(plugin, getKey().toString());
    namespaceMap.put(getKey(), namespacedKey);
  }

  abstract public DefinedNamespace getKey();

}