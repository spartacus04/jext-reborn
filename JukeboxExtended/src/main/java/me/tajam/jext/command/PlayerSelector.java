package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tajam.jext.SMS;

class PlayerSelector {

  private enum Selector {
    ALL,
    WORLD,
    NEARBY,
    WORLD_RANDOM,
    RANDOM,
    SELF,
    PLAYER
  }

  private static final HashMap<String, Selector> SELECTOR_SYMBOL_MAP;
  static {
    SELECTOR_SYMBOL_MAP = new HashMap<>();
    SELECTOR_SYMBOL_MAP.put("@a", Selector.ALL);
    SELECTOR_SYMBOL_MAP.put("@w", Selector.WORLD);
    SELECTOR_SYMBOL_MAP.put("@n", Selector.NEARBY);
    SELECTOR_SYMBOL_MAP.put("@wr", Selector.WORLD_RANDOM);
    SELECTOR_SYMBOL_MAP.put("@r", Selector.RANDOM);
    SELECTOR_SYMBOL_MAP.put("@s", Selector.SELF);
  }
  
  static Set<String> getSelectorStrings() {
    return SELECTOR_SYMBOL_MAP.keySet();
  }

  private CommandSender sender;
  private Selector mode;
  private String selector;

  PlayerSelector(CommandSender sender, String selector) {
    this.sender = sender;
    this.selector = selector;
    this.mode = SELECTOR_SYMBOL_MAP.get(selector);
    if (this.mode == null) {
      this.mode = Selector.PLAYER;
    }
  }

  List<Player> getPlayers() {
    try {
      switch(mode) {
        case ALL: return getAllPlayers();
        case WORLD: return getWorldPlayers();
        case WORLD_RANDOM: return getRandomPlayers(getWorldPlayers());
        case RANDOM: return getRandomPlayers(getAllPlayers());
        case SELF: return getSelf();
        case PLAYER: {
          final List<Player> players = new ArrayList<>();
          final Player player = Bukkit.getPlayer(selector);
          if (player == null) {
            new SMS().eror().t("Cannot find player: ").o(selector).send(sender);
            return null;
          }
          players.add(player);
          return players;
        }
        default: return null;
      }
    } catch (IllegalStateException e) {
      new SMS().eror().t("Invalid selector for console!").send(sender);
      return null;
    }
    
  }

  private List<Player> getAllPlayers() {
    return new ArrayList<>(Bukkit.getOnlinePlayers());
  }

  private List<Player> getWorldPlayers() throws IllegalStateException {
    if (sender instanceof Player) {
      Player player = (Player)sender;
      return player.getWorld().getPlayers(); 
    }
    throw new IllegalStateException();
  }

  private List<Player> getRandomPlayers(List<Player> players) {
    Collections.shuffle(players);
    return players.subList(0, 1); 
  }

  private List<Player> getSelf() throws IllegalStateException {
    if (sender instanceof Player) {
      final List<Player> players = new ArrayList<>();
      players.add((Player)sender);
      return players;
    }
    throw new IllegalStateException();
  }

}