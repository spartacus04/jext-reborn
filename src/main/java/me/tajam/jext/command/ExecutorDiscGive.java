package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.DiscContainer;
import me.tajam.jext.Log;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class ExecutorDiscGive extends ExecutorAdapter {

  ExecutorDiscGive() {
    super("discgive");
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

  private boolean mergedExecute(CommandSender sender, String[] args) {
    final List<Player> players = new PlayerSelector(sender, args[0]).getPlayers();
    if (players == null) return true;

    final DiscContainer disc = ConfigDiscManager.getInstance().getDisc(args[1]);
    if (disc == null) {
      new Log().eror().t("Disc with the namespace ").o(args[1]).t(" doesn't exists.").send(sender);
      return true;
    }

    for (Player player : players) {
      player.getInventory().addItem(disc.getDiscItem());
      new Log().info().t("Received ").p().t(" disc.").send(player, disc);
    }

    final Integer playerCount = players.size();
    if (playerCount >= 2) {
      new Log().warn().t("Given ").o().t(" disc to ").o().t(" players!").send(sender, disc, playerCount);
    } else if (playerCount == 1) {
      new Log().okay().t("Given ").o().t(" disc to ").o(players.get(0).getDisplayName()).t(".").send(sender, disc);
    } else {
      new Log().eror().t("Given disc to no player, something might when wrong!").send(sender);
    }
    return true;
  }
}