package com.tajam.jext.command;

import java.util.logging.Logger;

import com.tajam.jext.config.ConfigDiscManager;
import com.tajam.jext.disc.DiscContainer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecutorDisc extends ExecutorAdapter {

  Logger logger;

  public ExecutorDisc(Logger logger, String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
    this.logger = logger;
  }

  @Override
  protected boolean executePlayer(Player sender, int argv, String[] args) {
    ConfigDiscManager manager = ConfigDiscManager.getInstance();
    DiscContainer disc = manager.getDisc(args[0]);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That disc doesn't exists.");
      return true;
    }
    sender.getInventory().addItem(disc.getDiscItem());
    return true;
  }

  @Override
  protected boolean executeCommand(CommandSender sender, int argv, String[] args) {
    logger.info("This command is only for players.");
    return true;
  }

}