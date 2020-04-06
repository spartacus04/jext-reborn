package com.tajam.jext.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ResourceStatusListener implements Listener {

  @EventHandler()
  public void onResourceStatus(PlayerResourcePackStatusEvent e) {
    Status status = e.getStatus();

    if (status == PlayerResourcePackStatusEvent.Status.DECLINED) {
      e.getPlayer().kickPlayer("Config kick message here");
      return;
    }

    if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
      e.getPlayer().kickPlayer("Config kick message here");
      return;
    }
  }

}