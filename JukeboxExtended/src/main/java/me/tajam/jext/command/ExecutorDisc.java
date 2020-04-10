package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.DiscContainer;
import me.tajam.jext.SMS;

import org.bukkit.entity.Player;

class ExecutorDisc extends ExecutorAdapter {

  ExecutorDisc() {
    super("disc");
    addParameter(new Parameter("namespace", new CompletorDisc()));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final DiscContainer disc = manager.getDisc(args[0]);
    if (disc == null) {
      new SMS().eror().t("Disc with the namespace ").o(args[0]).t(" doesn't exists.").send(sender);
      return true;
    }
    sender.getInventory().addItem(disc.getDiscItem());
    return true;
  }
}