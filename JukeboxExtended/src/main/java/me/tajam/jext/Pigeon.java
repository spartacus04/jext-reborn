package me.tajam.jext;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pigeon {

  private final String letter;
  private final CommandSender receiver;

  public Pigeon(String message, CommandSender target) {
    letter = message;
    receiver = target;
  }

  public Pigeon(String message) {
    this(message, null);
  }

  public void deliver() {
    if (receiver == null || !(receiver instanceof Player)) {
      Bukkit.getConsoleSender().sendMessage(letter);
    } else {
      Player player = (Player)receiver;
      player.sendMessage(letter);
    }
  }

}