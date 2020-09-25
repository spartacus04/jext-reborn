package me.tajam.jext;

import me.tajam.jext.command.CommandsRegistrant;
import me.tajam.jext.config.ConfigManager;
import me.tajam.jext.configuration.ConfigFile;
import me.tajam.jext.configuration.ConfigYmlvLegacy;
import me.tajam.jext.listener.ListenersRegistrant;

import java.util.Map.Entry;

import org.bukkit.plugin.java.JavaPlugin;

public class Jext extends JavaPlugin {

  private static final Log ENABLED_MESSAGE = new Log().okay().t("Enabled Jukebox Extender, Do Re Mi!");
  private static final Log DISABLED_MESSAGE = new Log().warn().t("Disabled Jukebox Extender, Mi Re Do!");

  @Override
  public void onEnable() {
    try {
      new SpigotVersion(this);
      load();
    } catch (Exception e) {
      e.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }
    ENABLED_MESSAGE.send();
  }

  @Override
  public void onDisable() {
    DISABLED_MESSAGE.send();
  }

  private void load() {

    // Register namespaces
    JextNamespace.registerNamespace(this);

    // Load configurations
    ConfigManager.getInstance().setPlugin(this).load();

    // Setup commands
    CommandsRegistrant.getInstance().registerCommands(this);

    // Register listeners
    ListenersRegistrant.getInstance().registerListeners(this);

    //test
    new ConfigFile(ConfigYmlvLegacy.class).load(this);
    System.out.println(ConfigYmlvLegacy.Jext.FORCE_RESOURCE_PACK);
    System.out.println(ConfigYmlvLegacy.Jext.RESOURCE_PACK_DECLINE_KICK_MESSAGE);
    System.out.println(ConfigYmlvLegacy.Jext.IGNORE_FAILED_DOWNLOAD);
    System.out.println(ConfigYmlvLegacy.Jext.FAILED_DOWNLOAD_KICK_MESSAGE);
    System.out.println(ConfigYmlvLegacy.Jext.ALLOW_MUSIC_OVERLAPPING);
    for (Entry<String, ConfigYmlvLegacy.DiscData> entry : ConfigYmlvLegacy.Jext.DISC.entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue().NAMESPACE);
      System.out.println(entry.getValue().AUTHOR);
      System.out.println(entry.getValue().MODEL_DATA);
      System.out.println(entry.getValue().CREEPER_DROP);
      for (String lore: entry.getValue().LORE) {
        System.out.println(lore);
      }
    }
  }

}