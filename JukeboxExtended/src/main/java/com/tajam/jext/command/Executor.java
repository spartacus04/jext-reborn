package com.tajam.jext.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

abstract class Executor implements CommandExecutor {

  private String permissionString;
  private int[] argsLength;

  public Executor(String permissionString, int[] argsLength) {
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

    if (sender instanceof Player) return execute((Player)sender, argv, args);
    return execute(sender, argv, args);
  }

  protected abstract boolean execute(CommandSender sender, int argv, String[] args);
  protected abstract boolean execute(Player sender, int argv, String[] args);
}