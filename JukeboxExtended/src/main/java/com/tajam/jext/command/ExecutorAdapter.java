package com.tajam.jext.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class ExecutorAdapter implements CommandExecutor {

  private String permissionString;
  private int[] argsLength;

  public ExecutorAdapter(String permissionString, int[] argsLength) {
    this.permissionString = permissionString;
    this.argsLength = argsLength;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!sender.hasPermission(permissionString)) {
      sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
      return true;
    }

    int argv = args.length;
    boolean occur = false;
    for (int c: argsLength) {
      if (c == argv) {
        occur = true;
        break;
      }
    }
    if (!occur) return false;

    if (sender instanceof Player) return executePlayer((Player)sender, argv, args);
    return executeCommand(sender, argv, args);
  }

  protected boolean executeCommand(CommandSender sender, int argv, String[] args) {
    return false;
  }

  protected boolean executePlayer(Player sender, int argv, String[] args) {
    return false;
  }
}