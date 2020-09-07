package me.tajam.jext.command;

import java.util.List;

import org.bukkit.command.CommandSender;

interface Completor {

  public List<String> onComplete(String parameter, CommandSender sender);

}