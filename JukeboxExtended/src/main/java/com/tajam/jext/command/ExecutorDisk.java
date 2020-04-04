package com.tajam.jext.command;

import java.util.logging.Logger;

import com.tajam.jext.Disc;
import com.tajam.jext.DiscBuffer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ExecutorDisk extends Executor {

  Logger logger;
  DiscBuffer buffer;

  public ExecutorDisk(Logger logger, DiscBuffer buffer, String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
    this.logger = logger;
    this.buffer = buffer;
  }

  // From player
  @Override
  protected boolean execute(Player sender, int argv, String[] args) {
    Disc disc = buffer.getDisc(args[0]);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That disc doesn't exists.");
      return true;
    }
    sender.getInventory().addItem(disc.makeDisc());
    return true;
  }

  // From non-player
  @Override
  protected boolean execute(CommandSender sender, int argv, String[] args) {
    logger.info("This command is only for players.");
    return true;
  }

}