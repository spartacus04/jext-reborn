package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CompletorLocation implements Completor {

  enum Axis {
    X, Y, Z
  }

  private Axis axis;

  CompletorLocation(Axis axis) {
    this.axis = axis;
  }

  @Override
  public List<String> onComplete(String parameter, CommandSender sender) {
    final List<String> suggestions = new ArrayList<>();
    suggestions.add("~");
    final Player player = (sender instanceof Player)? (Player)sender : null;
    if (player == null) return suggestions;
    final Block block = player.getTargetBlockExact(4);
    if (block == null) return suggestions;
    final Location location = block.getLocation();
    switch(axis) {
      case X: {
        Integer a = location.getBlockX();
        suggestions.add(a.toString());
        break;
      }
      case Y: {
        Integer a = location.getBlockY();
        suggestions.add(a.toString());
        break;
      }
      case Z: {
        Integer a = location.getBlockZ();
        suggestions.add(a.toString());
        break;
      }
      default:
        break;
    }
    return suggestions;
  }

}