package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.disc.DiscContainer;
import me.tajam.jext.Log;

import org.bukkit.entity.Player;

class ExecutorDisc extends ExecutorAdapter {

  ExecutorDisc() {
    super("disc");
    addParameter(new ParameterDisc(true));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    final DiscContainer disc = ConfigDiscManager.getInstance().getDisc(args[0]);
    if (disc == null) {
      new Log().eror().t("Disc with the namespace ").o(args[0]).t(" doesn't exists.").send(sender);
      return true;
    }
    sender.getInventory().addItem(disc.getDiscItem());
    new Log().info().t("Obtained ").p().t(" disc.").send(sender, disc);
    return true;
  }
}