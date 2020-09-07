package me.tajam.jext.listener;

import me.tajam.jext.config.ConfigData;
import me.tajam.jext.config.ConfigManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

class ResourceStatusListener implements Listener {

  private JavaPlugin plugin;

  public ResourceStatusListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler()
  public void onResourceStatus(PlayerResourcePackStatusEvent e) {
    Status status = e.getStatus();
    ConfigManager manager = ConfigManager.getInstance();
    new BukkitRunnable(){
      @Override
      public void run() {
        if (status == PlayerResourcePackStatusEvent.Status.DECLINED && manager.getBooleanData(ConfigData.BooleanData.Path.FORCE_PACK)) {
          e.getPlayer().kickPlayer(manager.getStringData(ConfigData.StringData.Path.KICK_DECLINE_MESSAGE));
          return;
        }
    
        if (status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD && !manager.getBooleanData(ConfigData.BooleanData.Path.IGNORE_FAIL)) {
          e.getPlayer().kickPlayer(manager.getStringData(ConfigData.StringData.Path.KICK_FAIL_MESSAGE));
          return;
        }
      }
    }.runTaskLater(plugin, 100);
    
  }

}