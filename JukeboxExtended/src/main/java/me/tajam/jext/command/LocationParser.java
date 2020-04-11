package me.tajam.jext.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocationParser {

  private String stringX;
  private String stringY;
  private String stringZ;
  private Player player;

  public LocationParser(String stringX, String stringY, String stringZ, CommandSender sender) throws IllegalStateException {
    this.stringX = stringX;
    this.stringY = stringY;
    this.stringZ = stringZ;
    if (!(sender instanceof Player)) throw new IllegalStateException();
    this.player = (Player)sender;
  }

  public Location parse() {
    final int intX;
    if (stringX.equals("~")) {
      intX = player.getLocation().getBlockX();
    } else {
      intX = (int)Float.parseFloat(stringX);
    }

    final int intY;
    if (stringY.equals("~")) {
      intY = player.getLocation().getBlockX();
    } else {
      intY = (int)Float.parseFloat(stringY);
    }

    final int intZ;
    if (stringZ.equals("~")) {
      intZ = player.getLocation().getBlockX();
    } else {
      intZ = (int)Float.parseFloat(stringZ);
    }

    return new Location(player.getWorld(), intX, intY, intZ);
  }
}