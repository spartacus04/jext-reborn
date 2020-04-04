package com.tajam.jext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JextAPI {
  
  private static JextAPI instance = null;
  public static JextAPI getInstance() {
    if (instance == null) {
      instance = new JextAPI();
    }
    return instance;
  }

  public static final double JUKEBOX_RANGE = 66.0;
  public static final float JUKEBOX_VOLUME = 3.0f;

  private ProtocolManager protocolManager;

  private JextAPI() {}

  public void setProtocolManager(ProtocolManager protocolManager) {
    this.protocolManager = protocolManager;
  }

  public boolean isCustomDisc(ItemStack disc) {
    if (disc == null || disc.getType() != Material.MUSIC_DISC_CAT || !disc.hasItemMeta()) {
      return false;
    }

    ItemMeta meta = disc.getItemMeta();
    if (!meta.hasLore()) return false;
    ArrayList<String> lores = new ArrayList<>(meta.getLore());

    try {
      // Look for disc specifier
      if (!lores.get(0).equals(Disc.SPECIFIER)) {
        System.out.println(lores.get(0));
        System.out.println(Disc.SPECIFIER);
        return false;
      }
    } catch (IndexOutOfBoundsException e) {
      return false;
    }

    return true;
  }

  public void stopPlaying(ItemStack disc, Location location) {
    final String nsid =  nsidDecoder(disc);
    stopPlaying(nsid, location);
  }

  public void stopPlaying(String nsid, Location location) {
    for (Player player : location.getWorld().getPlayers()) {
      if (player.getLocation().distance(location) < JUKEBOX_RANGE) {
        player.stopSound(nsid, SoundCategory.RECORDS);
      }
    }
  }

  public void stopOriginalPlaying(Location location) {
    // Stop the original sound
    PacketContainer stopSoundPacket = protocolManager.createPacket(PacketType.Play.Server.WORLD_EVENT);
    stopSoundPacket.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    stopSoundPacket.getIntegers().write(0, 1010).write(1, 0);
    stopSoundPacket.getBooleans().write(0, false);
    for (Player player : location.getWorld().getPlayers()) {
      if (player.getLocation().distance(location) < JUKEBOX_RANGE) {
        try {
          protocolManager.sendServerPacket(player, stopSoundPacket);
        } catch (InvocationTargetException e) {
          player.stopSound(Sound.MUSIC_DISC_CAT, SoundCategory.RECORDS);
        }
      }
    }
  }

  public String nsidDecoder(ItemStack disc) {
    ItemMeta meta = disc.getItemMeta();
    ArrayList<String> lores = new ArrayList<>(meta.getLore());
    String id = "";
    try {
      String enc = lores.get(2);
      for (Character c : enc.toCharArray()) {
        if (!c.equals(ChatColor.COLOR_CHAR)) {
          id += c.toString();
        }
      }
    } catch (IndexOutOfBoundsException e) {
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return id;
  }

}