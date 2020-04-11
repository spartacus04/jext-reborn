package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

class CompletorFloat implements Completor {

  private final Float[] suggestedValues;

  CompletorFloat(Float... suggestedValues) {
    this.suggestedValues = suggestedValues;
  }

  @Override
  public List<String> onComplete(String parameter, CommandSender sender) {
    if (parameter.isEmpty()) {
      List<String> suggestions = new ArrayList<>();
      for (Float value : suggestedValues) {
        suggestions.add(value.toString());
      }
      return suggestions;
    }
    return null;
  }

}