package com.tajam.jext;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class JextListener implements Listener {

  private JextAPI api;

  public JextListener() {
    api = JextAPI.getInstance();
  }

  @EventHandler(ignoreCancelled = true)
  public void onJukeboxInteract(final PlayerInteractEvent e) {
    final Block block = e.getClickedBlock();
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK || block == null || block.getType() != Material.JUKEBOX) {
      return;
    }
    final BlockState state = block.getState();
    if (!(state instanceof Jukebox)) return;

    final Jukebox jukebox = (Jukebox)state;
    final Player player = e.getPlayer();
    final ItemStack item = e.getItem();
    final Location location = block.getLocation();

    // Eject the disc if a custom disc is inside
    final ItemStack disc = jukebox.getRecord();
    if (api.isCustomDisc(disc)) {
      api.stopPlaying(disc, location);
      return;
    }

    // Allow the disc to get out normally
    if (jukebox.getRecord().getType() != Material.AIR) return;

    // Check if the holding item is a custom disc
    if (!api.isCustomDisc(item)) return;

    /* ======================================================================== \
      If you are wondering how the plugin stop the original sound from playing.
      Basically the packets are filtered in MusicPacketListener.java.
      That's about it.
    \ ======================================================================== */

    // Play the custom song
    final String nsid = api.nsidDecoder(item);
    player.getWorld().playSound(location, nsid, SoundCategory.RECORDS, JextAPI.JUKEBOX_VOLUME, 1.0f);
  }

  @EventHandler(ignoreCancelled = true)
  public void onJukeboxBreak(final BlockBreakEvent e) {
    final Block block = e.getBlock();
    final BlockState state = block.getState();
    if (!(state instanceof Jukebox)) return;
    final Jukebox jukebox = (Jukebox)state;
    final ItemStack disc = jukebox.getRecord();

    // Check if the inserted disc is a custom disc
    if (!api.isCustomDisc(disc)) return;

    api.stopPlaying(disc, block.getLocation());
  }

}