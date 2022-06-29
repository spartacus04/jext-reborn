package me.spartacus04.jext.command;

import me.spartacus04.jext.Log;
import me.spartacus04.jext.config.ConfigDiscManager;
import me.spartacus04.jext.disc.DiscContainer;
import me.spartacus04.jext.disc.DiscPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class ExecutorPlayAt extends ExecutorAdapter {

  ExecutorPlayAt() {
    super("playat");
    addParameter(new ParameterLocation(true, ParameterLocation.Axis.X));
    addParameter(new ParameterLocation(true, ParameterLocation.Axis.Y));
    addParameter(new ParameterLocation(true, ParameterLocation.Axis.Z));
    addParameter(new ParameterDisc(true));
    addParameter(new ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"));
    addParameter(new ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    final Location location;
    try {
      location = new LocationParser(args[0], args[1], args[2], sender).parse();
    } catch (NumberFormatException e) {
      new Log().eror().t("Invalid location value!").send(sender);
      return true;
    }

    final DiscContainer disc = ConfigDiscManager.getInstance().getDisc(args[3]);
    if (disc == null) {
      new Log().eror().t("Disc with the namespace ").o(args[3]).t(" doesn't exists.").send(sender);
      return true;
    }
    final DiscPlayer discPlayer = new DiscPlayer(disc);

    if (args.length >= 5) {
      try {
        final float pitch = Float.parseFloat(args[4]);
        discPlayer.setPitch(pitch);
      } catch (NumberFormatException e) {
        new Log().eror().t("Wrong number format for pitch parameter.").send(sender);
        return true;
      }
    }

    if (args.length >= 6) {
      try {
        final float volume = Float.parseFloat(args[5]);
        discPlayer.setVolume(volume);
      } catch (NumberFormatException e) {
        new Log().eror().t("Wrong number format for volume parameter.").send(sender);
        return true;
      }
    }

    discPlayer.play(location);
    return true;
  }
}