package me.tajam.jext.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.tajam.jext.SpigotVersion;
import me.tajam.jext.config.ConfigDiscManager;
import me.tajam.jext.disc.DiscContainer;

public class CreeperDeathListener implements Listener {

  private List<DiscContainer> droppableDiscs = new ArrayList<>();
  private Random generator = new Random();

  public CreeperDeathListener() {
    final ConfigDiscManager configDiscManager = ConfigDiscManager.getInstance();
    final Set<String> namespaces = configDiscManager.getNamespaces();
    for (String namespace : namespaces) {
      DiscContainer discContainer = configDiscManager.getDisc(namespace);
      if (discContainer.canCreeperDrop()) {
        droppableDiscs.add(discContainer);
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onCreeperDeath(final EntityDeathEvent event)
  {
    if (!(event.getEntity() instanceof Creeper)) return;
    List<ItemStack> drops = event.getDrops();
    ItemStack disc = null;
    for (ItemStack item : drops) {
      if (item.getType().isRecord()) {
        disc = item;
        break;
      }
    }
    if (disc != null) {
      int discCount = DiscContainer.SOUND_MAP.size();
      if (SpigotVersion.isVersion1_16()) discCount--;
      final int index = generator.nextInt(this.droppableDiscs.size() + discCount);
      if (index < this.droppableDiscs.size()) {
        drops.remove(disc);
        drops.add(this.droppableDiscs.get(index).getDiscItem());
      }
    }
  }

}