package com.tajam.jext.command;

import com.tajam.jext.config.ConfigDiscManager;
import com.tajam.jext.disc.DiscContainer;
import com.tajam.jext.disc.DiscPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ExecutorStop extends ExecutorAdapter {

  public ExecutorStop(ConsoleCommandSender consoleSender, String permissionString, int[] argsLength) {
    super(consoleSender, permissionString, argsLength);
  }

  @Override
  protected boolean executePlayer(Player sender, int argv, String[] args) {
    if (argv == 0) {
      return stopAll(sender);
    }

    if (argv == 1) {
      return stopOne(sender, args[0]);
    }

    sender.sendMessage(ChatColor.DARK_RED + "Invalid parameters!");
    return true;
  }

  private boolean stopAll(Player sender) {
    ConfigDiscManager manager = ConfigDiscManager.getInstance();
    for (String key : manager.getNamespaces()) {
      DiscContainer disc = manager.getDisc(key);
      DiscPlayer player = new DiscPlayer(disc, sender.getLocation());
      player.stop();
    }
    return true;
  }

  private boolean stopOne(Player sender, String namespace) {
    ConfigDiscManager manager = ConfigDiscManager.getInstance();
    DiscContainer disc = manager.getDisc(namespace);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That music doesn't exists.");
      return true;
    }
    DiscPlayer player = new DiscPlayer(disc, sender.getLocation());
    player.stop();
    return true;
  }

}