package me.tajam.jext.command.tab;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletorAdaptor implements TabCompleter {

  private ArrayList<Completor> completors;

  public TabCompletorAdaptor() {
    completors = new ArrayList<>();
  }

  public TabCompletorAdaptor addCompletor(Completor completor) {
    this.completors.add(completor);
    return this;
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