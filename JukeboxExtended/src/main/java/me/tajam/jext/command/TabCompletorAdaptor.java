package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

class TabCompletorAdaptor implements TabCompleter {

  private ArrayList<Completor> completors;

  TabCompletorAdaptor() {
    completors = new ArrayList<>();
  }

  void addCompletor(Completor completor) {
    this.completors.add(completor);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    try {
      final int index = args.length - 1;
      final Completor completor = completors.get(index);
      return completor.onComplete(args[index]);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }
  
}