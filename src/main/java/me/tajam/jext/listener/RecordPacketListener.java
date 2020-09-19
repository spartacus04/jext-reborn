package me.tajam.jext.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;

import me.tajam.jext.disc.DiscContainer;
import me.tajam.jext.Log;
import me.tajam.jext.SpigotVersion;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

class RecordPacketListener extends PacketAdapter {

  public RecordPacketListener(Plugin plugin, ListenerPriority priority) {
    super(plugin, priority, new PacketType[] { PacketType.Play.Server.WORLD_EVENT });
  }

  @Override
  public void onPacketSending(PacketEvent event) {

    final PacketContainer packet = event.getPacket();
    final StructureModifier<BlockPosition> position = packet.getBlockPositionModifier();
    final BlockPosition blockPosition = position.getValues().get(0);
    final Player player = event.getPlayer();
    final World world = player.getWorld();
    final Location location = new Location(world, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    final Block block = world.getBlockAt(location);
    final BlockState blockState = block.getState();
    final Integer data = packet.getIntegers().read(1);
    
    if (blockState instanceof Jukebox && !data.equals(0)) {
      final Jukebox jukebox = (Jukebox) blockState;
      final ItemStack disc = jukebox.getRecord();
      final DiscContainer container;
      try {
        container = new DiscContainer(disc);
      } catch (IllegalStateException e) {
        return;
      }
      new BukkitRunnable() {
        @Override
        public void run() {
          player.stopSound(DiscContainer.SOUND_MAP.get(container.getMaterial()), SoundCategory.RECORDS);
          if (SpigotVersion.isVersion1_15() || SpigotVersion.isVersion1_16()) {
            ActionBarDisplay_LT15(player, container);
          } else {
            ActionBarDisplay_ST14(player, container);
          }
        }
      }.runTaskLater(plugin, 4);
    }
  }

  public void ActionBarDisplay_LT15 (Player player, DiscContainer container) {
    final BaseComponent[] baseComponents =  new ComponentBuilder()
      .append("Now playing: ").color(ChatColor.GOLD)
      .append(container.getAuthor()).color(ChatColor.GREEN)
      .append(" - ").color(ChatColor.GRAY)
      .append(container.toString()).color(ChatColor.GOLD)
      .create();
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponents);
  }

  public void ActionBarDisplay_ST14 (Player player, DiscContainer container) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
      new Log().y("Now playing: ").g(container.getAuthor()).gr(" - ").y(container.toString()).text()
    ));
  }
}