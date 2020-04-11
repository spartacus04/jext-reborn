package me.tajam.jext.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;

import me.tajam.jext.DiscContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

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

public class RecordPacketListener extends PacketAdapter {

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
    
    if (blockState instanceof Jukebox) {
      final Jukebox jukebox = (Jukebox) blockState;
      final ItemStack disc = jukebox.getRecord();
      final DiscContainer container;
      try {
        container = new DiscContainer(disc);
      } catch (IllegalStateException e) {
        return;
      }
      new BukkitRunnable(){
        @Override
        public void run() {
          player.stopSound(DiscContainer.BASEDISC_SOUND, SoundCategory.RECORDS);
          BaseComponent[] baseComponents = new ComponentBuilder()
            .append("Now playing: ")
            .color(ChatColor.GOLD)
            .append(container.getAuthor())
            .color(ChatColor.GREEN)
            .append(" - " + container.toString())
            .color(ChatColor.GOLD)
            .create();
          player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponents);
        }
      }.runTaskLater(plugin, 1);
    }

  }
}