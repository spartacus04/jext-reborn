package me.tajam.jext.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class ParameterNumber extends Parameter {

  private final List<String> stringValues;
  private String name;

  ParameterNumber(boolean required, Float... suggestedValues) {
    super(required);
    this.stringValues = new ArrayList<>();
    for (Float value : suggestedValues) {
      this.stringValues.add(value.toString());
    }
  }

  ParameterNumber(boolean required, Integer... suggestedValues) {
    super(required);
    this.stringValues = new ArrayList<>();
    for (Integer value : suggestedValues) {
      this.stringValues.add(value.toString());
    }
  }

  ParameterNumber setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  String getName() {
    return this.name == null ? this.name : "number";
  }

  @Override
  List<String> onComplete(String parameter, CommandSender sender) {
    if (parameter.isEmpty()) {
      return stringValues;
    }
    return null;
  }
  
}
