package com.tajam.jext.command;

import java.util.logging.Logger;

import com.tajam.jext.config.ConfigDiscManager;
import com.tajam.jext.disc.DiscContainer;
import com.tajam.jext.disc.DiscPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecutorPlay extends ExecutorAdapter {

  Logger logger;

  public ExecutorPlay(Logger logger, String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
    this.logger = logger;
  }

  @Override
  protected boolean executePlayer(Player sender, int argv, String[] args) {
    ConfigDiscManager manager = ConfigDiscManager.getInstance();
    DiscContainer disc = manager.getDisc(args[0]);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That music doesn't exists.");
      return true;
    }
    DiscPlayer player = new DiscPlayer(disc, sender.getLocation());
    player.play();
    return true;
  }

  @Override
  protected boolean executeCommand(CommandSender sender, int argv, String[] args) {
    logger.info("This command is only for players.");
    return true;
  }

}