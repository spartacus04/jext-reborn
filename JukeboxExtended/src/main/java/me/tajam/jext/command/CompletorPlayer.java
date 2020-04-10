package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class CompletorPlayer implements Completor {

  @Override
  public List<String> onComplete(String parameter) {
    final List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
    final Set<String> selectors = PlayerSelector.getSelectorStrings();
    final List<String> matches = new ArrayList<>();
    for (String selector : selectors) {
      if (matches.size() >= 12) break;
      if (selector.startsWith(parameter.toLowerCase())) {
        matches.add(selector);
      }
    }
    for (Player player : players) {
      if (matches.size() >= 12) break;
      final String name = player.getDisplayName();
      if (name.toLowerCase().startsWith(parameter.toLowerCase())) {
        matches.add(name);
      }
    }
    if (matches.size() > 0) return matches;
    return null;
  }

}