package me.tajam.jext.listener;

import me.tajam.jext.DiscContainer;
import me.tajam.jext.DiscPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class JukeboxEventListener implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onJukeboxInteract(final PlayerInteractEvent event) {
    final Block block = event.getClickedBlock();
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK || block == null || block.getType() != Material.JUKEBOX) {
      return;
    }
    final BlockState state = block.getState();
    if (!(state instanceof Jukebox)) return;

    final Jukebox jukebox = (Jukebox)state;
    final Location location = block.getLocation();

    // Eject the disc and stop the music if a custom disc is inside
    try {
      final ItemStack disc = jukebox.getRecord();
      final DiscContainer discContainer = new DiscContainer(disc);
      final DiscPlayer discPlayer = new DiscPlayer(discContainer);
      discPlayer.stop(location);
      return;
    } catch (IllegalStateException e) { }

    // Allow the disc to get out normally
    if (jukebox.getRecord().getType() != Material.AIR) return;

    // Try to play the custom song
    try {
      final ItemStack disc = event.getItem();
      final DiscContainer discContainer = new DiscContainer(disc);
      final DiscPlayer discPlayer = new DiscPlayer(discContainer);
      discPlayer.play(location);
    } catch (IllegalStateException e) { }
  }

  @EventHandler(ignoreCancelled = true)
  public void onJukeboxBreak(final BlockBreakEvent event) {
    final Block block = event.getBlock();
    final BlockState state = block.getState();
    if (!(state instanceof Jukebox)) return;

    try {
      final Jukebox jukebox = (Jukebox)state;
      final ItemStack disc = jukebox.getRecord();
      final DiscContainer discContainer = new DiscContainer(disc);
      final DiscPlayer discPlayer = new DiscPlayer(discContainer);
      discPlayer.stop(block.getLocation());
      return;
    } catch (IllegalStateException e) { }
  }

}