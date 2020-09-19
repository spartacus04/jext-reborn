package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import me.tajam.jext.config.ConfigDiscManager;

class ParameterDisc extends Parameter {

  ParameterDisc(boolean required) {
    super(required);
  }

  @Override
  String getName() {
    return "namespace";
  }

  @Override
  List<String> onComplete(String parameter, CommandSender sender) {
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
