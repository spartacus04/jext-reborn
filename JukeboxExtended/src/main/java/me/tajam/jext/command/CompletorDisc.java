package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.tajam.jext.config.ConfigDiscManager;

class CompletorDisc implements Completor {

  @Override
  public List<String> onComplete(String parameter) {
    final Set<String> namespaces = ConfigDiscManager.getInstance().getNamespaces();
    final List<String> matches = new ArrayList<>();
    for (final String namespace : namespaces) {
      if (matches.size() >= 8) break;
      if (namespace.toLowerCase().startsWith(parameter.toLowerCase())) {
        matches.add(namespace);
      }
    }
    if (matches.size() > 0) return matches;
    return null;
  }
}