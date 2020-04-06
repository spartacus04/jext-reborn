package me.tajam.jext.command;

import me.tajam.jext.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecutorAdapter implements CommandExecutor {

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

    final int argv = args.length;
    boolean occur = false;
    for (int c: argsLength) {
      if (c == argv) {
        occur = true;
        break;
      }
    }
    if (!occur) return false;

    if (sender instanceof Player) return executePlayer((Player)sender, args);
    return executeCommand(sender, args);
  }

  protected boolean executePlayer(Player sender, String[] args) {
    sender.sendMessage(ChatColor.DARK_RED + "This command is only for console.");
    return true;
  }

  protected boolean executeCommand(CommandSender sender, String[] args) {
    Logger.info("This command is only for players.");
    return true;
  }
}