package me.tajam.jext.command;

import org.bukkit.ChatColor;

import me.tajam.jext.Log;

class Parameter {

  private final String name;
  private final Completor completor;
  private final boolean required;

  Parameter(String name, Completor completor, boolean required) {
    this.name = name;
    this.completor = completor;
    this.required = required;
  }

  Parameter(String name, Completor completor) {
    this(name, completor, true);
  }

  boolean isRequired() {
    return this.required;
  }

  Completor getCompletor() {
    return this.completor;
  }
  
  @Override
  public String toString() {
    Log sms = new Log().rst(((required)? "[" : "<")).rst().rst(((required)? "]" : ">"));
    return sms.text((required)? ChatColor.LIGHT_PURPLE + name : ChatColor.DARK_PURPLE + name);
  }

}