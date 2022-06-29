package me.spartacus04.jext.command;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandsRegistrant {

  private static CommandsRegistrant instance = null;
  public static CommandsRegistrant getInstance() {
    if (instance == null) {
      instance = new CommandsRegistrant();
    }
    return instance;
  }

  private CommandsRegistrant() {}

  public void registerCommands(JavaPlugin plugin) {
    new ExecutorDisc().registerTo(plugin);
    new ExecutorDiscGive().registerTo(plugin);
    new ExecutorPlayMusic().registerTo(plugin);
    new ExecutorPlayAt().registerTo(plugin);
    new ExecutorStopMusic().registerTo(plugin);
  }

}