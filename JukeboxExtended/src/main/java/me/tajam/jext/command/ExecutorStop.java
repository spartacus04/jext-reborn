package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.disc.DiscContainer;
import me.tajam.jext.disc.DiscPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ExecutorStop extends ExecutorAdapter {

  public ExecutorStop(String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
  }

  @Override
  protected boolean executePlayer(Player sender, String[] args) {
    if (args.length == 0) {
      return stopAll(sender);
    }

    if (args.length == 1) {
      return stopOne(sender, args[0]);
    }

    sender.sendMessage(ChatColor.DARK_RED + "Invalid parameters!");
    return true;
  }

  private boolean stopAll(Player sender) {
    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    for (String key : manager.getNamespaces()) {
      final DiscContainer disc = manager.getDisc(key);
      final DiscPlayer player = new DiscPlayer(disc, sender.getLocation());
      player.stop();
    }
    return true;
  }

  private boolean stopOne(Player sender, String namespace) {
    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final DiscContainer disc = manager.getDisc(namespace);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That music doesn't exists.");
      return true;
    }
    final DiscPlayer player = new DiscPlayer(disc, sender.getLocation());
    player.stop();
    return true;
  }

}