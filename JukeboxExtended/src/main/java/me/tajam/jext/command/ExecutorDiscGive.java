package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.DiscContainer;
import me.tajam.jext.SMS;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class ExecutorDiscGive extends ExecutorAdapter {

  ExecutorDiscGive() {
    super("discgive");
    addParameter(new Parameter("player", new CompletorPlayer()));
    addParameter(new Parameter("namespace", new CompletorDisc()));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    return mergedCommand(sender, args);
  }

  @Override
  boolean executeCommand(CommandSender sender, String[] args) {
    return mergedCommand(sender, args);
  }

  private boolean mergedCommand(CommandSender sender, String[] args) {
    final PlayerSelector selector = new PlayerSelector(sender, args[0]);
    final List<Player> players = selector.getPlayers();
    if (players == null) return true;

    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final DiscContainer disc = manager.getDisc(args[1]);
    if (disc == null) {
      new SMS().eror().t("Disc with the namespace ").o(args[1]).t(" doesn't exists.").send(sender);
      return true;
    }

    for (Player player : players) {
      player.getInventory().addItem(disc.getDiscItem());
      new SMS().info().t("Received ").p().t(" disc.").send(player, disc);
    }

    final Integer playerCount = players.size();
    if (playerCount > 2) {
      new SMS().warn().t("Given ").o().t(" disc to ").o().t(" players!").send(sender, disc, playerCount);
    } else if (playerCount == 1) {
      new SMS().okay().t("Given ").o().t(" disc to ").o(players.get(0).getDisplayName()).t(".").send(sender, disc);
    } else {
      new SMS().eror().t("Given disc to no player, something might when wrong!").send(sender);
    }
    return true;
  }
}