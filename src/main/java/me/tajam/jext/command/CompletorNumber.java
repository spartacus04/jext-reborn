package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

class CompletorNumber implements Completor {

  private final List<String> stringValues;

  CompletorNumber(Float... suggestedValues) {
    this.stringValues = new ArrayList<>();
    for (Float value : suggestedValues) {
      this.stringValues.add(value.toString());
    }
  }

  CompletorNumber(Integer... suggestedValues) {
    this.stringValues = new ArrayList<>();
    for (Integer value : suggestedValues) {
      this.stringValues.add(value.toString());
    }
  }

  @Override
  public List<String> onComplete(String parameter, CommandSender sender) {
    if (parameter.isEmpty()) {
      return stringValues;
    }
    return null;
  }

}