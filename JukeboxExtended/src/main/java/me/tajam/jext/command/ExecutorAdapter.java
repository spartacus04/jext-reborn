package me.tajam.jext.command;

import me.tajam.jext.SMS;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

class ExecutorAdapter implements CommandExecutor {

  private static final SMS DEFAULT_MESSAGE = new SMS().info().t("This command is only for ").t().t(".");
  private static final SMS PERMISSION_MESSAGE = new SMS().eror().t("You do not have permission to use this command.");

  private final TabCompletorAdaptor tabCompletor;
  private final String commandString;
  private final List<Parameter> parameters;

  ExecutorAdapter(String commandString) {
    this.tabCompletor = new TabCompletorAdaptor();
    this.parameters = new ArrayList<>();
    this.commandString = commandString;
  }

  void addParameter(Parameter parameter) throws IllegalArgumentException {
    if (parameters.size() > 0) {
      final Parameter lastParameter = parameters.get(parameters.size() - 1);
      if (!lastParameter.isRequired() && parameter.isRequired()) {
        throw new IllegalArgumentException("Required parameter cannot come after optional parameter!");
      }
    }
    parameters.add(parameter);
    tabCompletor.addCompletor(parameter.getCompletor());
  }

  void registerTo(JavaPlugin plugin) {
    final PluginCommand command = plugin.getCommand(commandString);
    command.setExecutor(this);
    command.setTabCompleter(tabCompletor);
    SMS usageSMS = new SMS("Usage").info().t("/").a(commandString);
    for (Parameter parameter : parameters) {
      usageSMS.rst(" ").rst(parameter.toString());
    }
    command.setUsage(usageSMS.text());
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!command.testPermissionSilent(sender)) {
      PERMISSION_MESSAGE.send(sender);
      return true;
    }

    if (args.length > parameters.size()) return false;
    if (args.length < parameters.size()) {
      if (args.length <= 0 || parameters.get(args.length - 1).isRequired()) {
        if (parameters.get(args.length).isRequired()) return false;
      }
    }

    if (sender instanceof Player) return executePlayer((Player)sender, args);
    return executeCommand(sender, args);
  }

  boolean executePlayer(Player sender, String[] args) {
    DEFAULT_MESSAGE.send(sender, "console");
    return true;
  }

  boolean executeCommand(CommandSender sender, String[] args) {
    DEFAULT_MESSAGE.send(sender, "players");
    return true;
  }
}