package com.tajam.jext.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.tajam.jext.JextAPI;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class RecordPacketListener extends PacketAdapter {

  JextAPI api;

  public RecordPacketListener(Plugin plugin, ListenerPriority priority) {
    super(plugin, priority, new PacketType[] {
      PacketType.Play.Server.WORLD_EVENT 
    });
    api = JextAPI.getInstance();
  }

  @Override
  public void onPacketSending(PacketEvent e) {

    final PacketContainer packet = e.getPacket();
    final StructureModifier<BlockPosition> position = packet.getBlockPositionModifier();
    final BlockPosition blockPosition = position.getValues().get(0);
    final Block block = e.getPlayer().getWorld().getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    final BlockState blockState = block.getState();

    if (blockState instanceof Jukebox) {
      final Jukebox jukebox = (Jukebox)blockState;
      final ItemStack disc = jukebox.getRecord();
      if (api.isCustomDisc(disc)) {
        e.setCancelled(true);
      }
    }

  }

}