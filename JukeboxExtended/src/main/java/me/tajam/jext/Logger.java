package me.tajam.jext;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public final class Logger {

  private static final String PLUGIN_NAME = "JukeboxExtended";
  private static final ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
  
  public static void info(String message) {
    ChatColor color = ChatColor.BLUE;
    consoleSender.sendMessage(headerGenerate(color) + " " + color + message);
  }

  public static void success(String message) {
    ChatColor color = ChatColor.GREEN;
    consoleSender.sendMessage(headerGenerate(color) + " " + color + message);
  }

  public static void warning(String message) {
    ChatColor color = ChatColor.YELLOW;
    consoleSender.sendMessage(headerGenerate(color) + " " + color + message);
  }

  public static void error(String message) {
    ChatColor color = ChatColor.RED;
    consoleSender.sendMessage(headerGenerate(color) + " " + color + message);
  }

  private static String headerGenerate(ChatColor color) {
    return "[" + color + PLUGIN_NAME + ChatColor.WHITE + "]";
  }

}