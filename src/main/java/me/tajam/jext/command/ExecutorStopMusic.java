package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.DiscContainer;
import me.tajam.jext.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class ExecutorStopMusic extends ExecutorAdapter {

  ExecutorStopMusic() {
    super("stopmusic");
    addParameter(new ParameterPlayer(true));
    addParameter(new ParameterDisc(true));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    return mergedExecute(sender, args);
  }

  @Override
  boolean executeCommand(CommandSender sender, String[] args) {
    return mergedExecute(sender, args);
  }

  boolean mergedExecute(CommandSender sender, String[] args) {
    final PlayerSelector selector = new PlayerSelector(sender, args[0]);
    final List<Player> players = selector.getPlayers();
    if (players == null) return true;

    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final Set<String> namespaces;
    if (args.length <= 1) {
      namespaces = manager.getNamespaces();
    } else {
      final DiscContainer disc = manager.getDisc(args[1]);
      if (disc == null) {
        new Log().eror().t("Music with the namespace ").o(args[1]).t(" doesn't exists.").send(sender);
        return true;
      }
      namespaces = new HashSet<String>();
      namespaces.add(disc.getNamespace());
    }
    
    for (Player player : players) {
      for (String namespace : namespaces) {
        player.stopSound(namespace, SoundCategory.RECORDS);
        if (namespaces.size() == 1)
          new Log().info().t("Stopped music ").p().t(".").send(player, manager.getDisc(namespace));
      }
      if (namespaces.size() > 1)
        new Log().info().t("Stopped all music.").send(player);
    }
    
    final Integer playerCount = players.size();
    if (playerCount >= 2) {
      new Log().warn().t("Stopped music for ").o().t(" players!").send(sender, playerCount);
    } else if (playerCount == 1) {
      new Log().okay().t("Stopped music for ").o(players.get(0).getName()).t(".").send(sender);
    } else {
      new Log().eror().t("Stopped music to no player, something might when wrong!").send(sender);
    }
    return true;
  }
}