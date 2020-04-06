package com.tajam.jext.command;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecutorStop extends ExecutorAdapter {

  Logger logger;

  public ExecutorStop(Logger logger, String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
    this.logger = logger;
  }

  @Override
  protected boolean executeCommand(CommandSender sender, int argv, String[] args) {

    return true;
  }

  @Override
  protected boolean executePlayer(Player sender, int argv, String[] args) {
    logger.info("This command is only for players.");
    return true;
  }

}