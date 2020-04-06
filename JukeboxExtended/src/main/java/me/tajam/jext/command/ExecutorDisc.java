package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.disc.DiscContainer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ExecutorDisc extends ExecutorAdapter {

  public ExecutorDisc(String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
  }

  @Override
  protected boolean executePlayer(Player sender, String[] args) {
    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final DiscContainer disc = manager.getDisc(args[0]);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That disc doesn't exists.");
      return true;
    }
    sender.getInventory().addItem(disc.getDiscItem());
    return true;
  }
}