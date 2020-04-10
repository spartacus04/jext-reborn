package me.tajam.jext.command;

import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.DiscContainer;
import me.tajam.jext.SMS;

import java.util.List;

import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

class ExecutorPlayMusic extends ExecutorAdapter {

  ExecutorPlayMusic() {
    super("playmusic");
    addParameter(new Parameter("player", new CompletorPlayer()));
    addParameter(new Parameter("namespace", new CompletorDisc()));
    addParameter(new Parameter("pitch", new CompletorFloat(1.0f, 1.5f, 0.5f), false));
  }

  @Override
  boolean executePlayer(Player sender, String[] args) {
    final PlayerSelector selector = new PlayerSelector(sender, args[0]);
    final List<Player> players = selector.getPlayers();
    if (players == null) return true;

    final ConfigDiscManager manager = ConfigDiscManager.getInstance();
    final DiscContainer disc = manager.getDisc(args[1]);
    if (disc == null) {
      new SMS().eror().t("Disc with the namespace ").o(args[1]).t(" doesn't exists.").send(sender);
      return true;
    }

    Float pitch = 1.0f;
    if (args.length >= 3) {
      try {
        pitch = Float.parseFloat(args[2]);
      } catch (NumberFormatException e) {
        new SMS().eror().t("Wrong number format for pitch parameter.");
        return true;
      }
    }

    for (Player player : players) {
      player.playSound(player.getLocation(), disc.getNamespace(), SoundCategory.MUSIC, Float.MAX_VALUE, pitch);
      new SMS().info().t("Music ").p().t(" is playing now.").send(player, disc);
    }
    
    final Integer playerCount = players.size();
    if (playerCount > 2) {
      new SMS().warn().t("Played music ").o().t(" to ").o().t(" players!").send(sender, disc, playerCount);
    } else if (playerCount == 1) {
      new SMS().okay().t("Played music ").o().t(" to ").o(players.get(0).getDisplayName()).t(".").send(sender, disc);
    } else {
      new SMS().eror().t("Played music to no player, something might when wrong!").send(sender);
    }
    return true;
  }
}