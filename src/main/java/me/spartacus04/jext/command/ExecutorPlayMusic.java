package me.spartacus04.jext.command;

import me.spartacus04.jext.Log;
import me.spartacus04.jext.config.ConfigDiscManager;
import me.spartacus04.jext.disc.DiscContainer;
import me.spartacus04.jext.disc.DiscPlayer;

import java.util.List;

import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class ExecutorPlayMusic extends ExecutorAdapter {

  ExecutorPlayMusic() {
    super("playmusic");
    addParameter(new ParameterPlayer(true));
    addParameter(new ParameterDisc(true));
    addParameter(new ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"));
    addParameter(new ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    return mergedExecute(sender, args);
  }

  @Override
  boolean executeCommand(CommandSender sender, String[] args) {
    return mergedExecute(sender, args);
  }

  private boolean mergedExecute(CommandSender sender, String[] args) {
    final List<Player> players = new PlayerSelector(sender, args[0]).getPlayers();
    if (players == null) return true;

    final DiscContainer disc = ConfigDiscManager.getInstance().getDisc(args[1]);
    if (disc == null) {
      new Log().eror().t("Disc with the namespace ").o(args[1]).t(" doesn't exists.").send(sender);
      return true;
    }
    final DiscPlayer discPlayer = new DiscPlayer(disc);

    float pitch = 1.0f;
    if (args.length >= 3) {
      try {
        pitch = Float.parseFloat(args[2]);
      } catch (NumberFormatException e) {
        new Log().eror().t("Wrong number format for pitch parameter.").send(sender);
        return true;
      }
    }

    boolean isMusic = true;
    if (args.length >= 4) {
      isMusic = false;
      try {
        final float volume = Float.parseFloat(args[3]);
        discPlayer.setPitch(pitch);
        discPlayer.setVolume(volume);
      } catch (NumberFormatException e) {
        new Log().eror().t("Wrong number format for volume parameter.").send(sender);
        return true;
      }
    }

    for (Player player : players) {
      if (isMusic) {
        player.playSound(player.getLocation(), disc.getNamespace(), SoundCategory.RECORDS, Float.MAX_VALUE, pitch);
        new Log().info().t("Music ").p().t(" is playing now.").send(player, disc);
      } else {
        discPlayer.play(player.getLocation());
      }
    }
    
    final Integer playerCount = players.size();
    if (playerCount >= 2) {
      new Log().warn().t("Played music ").o().t(" to ").o().t(" players!").send(sender, disc, playerCount);
    } else if (playerCount == 1) {
      new Log().okay().t("Played music ").o().t(" to ").o(players.get(0).getDisplayName()).t(".").send(sender, disc);
    } else {
      new Log().eror().t("Played music to no player, something might when wrong!").send(sender);
    }
    return true;
  }
}