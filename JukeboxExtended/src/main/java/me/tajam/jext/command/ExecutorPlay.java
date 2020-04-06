package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.disc.DiscContainer;
import me.tajam.jext.disc.DiscPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ExecutorPlay extends ExecutorAdapter {

  public ExecutorPlay(String permissionString, int[] argsLength) {
    super(permissionString, argsLength);
  }

  @Override
  protected boolean executePlayer(Player sender, String[] args) {
    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final DiscContainer disc = manager.getDisc(args[0]);
    if (disc == null) {
      sender.sendMessage(ChatColor.DARK_RED + "That music doesn't exists.");
      return true;
    }
    final DiscPlayer player = new DiscPlayer(disc, sender.getLocation());

    if (args.length > 1) {
      try {
        final Float value = Float.parseFloat(args[1]);
        player.setVolume(value);
      } catch (NumberFormatException e) {
        sender.sendMessage(ChatColor.DARK_RED + "Wrong number format for volume parameter.");
        return true;
      }
    }

    if (args.length > 2) {
      try {
        final Float value = Float.parseFloat(args[2]);
        player.setPitch(value);
      } catch (NumberFormatException e) {
        sender.sendMessage(ChatColor.DARK_RED + "Wrong number format for pitch parameter.");
        return true;
      }
    }

    player.play();
    return true;
  }
}