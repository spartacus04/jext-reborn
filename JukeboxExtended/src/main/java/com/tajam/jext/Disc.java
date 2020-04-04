package com.tajam.jext;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Disc {

  public static final String SPECIFIER = "§J§E§X§T";

  private String title;
  private String author;
  private String nsid;
  private int modelId;
  private ArrayList<String> lores;

  public Disc(String title, String author, String nsid, int modelId, ArrayList<String> lores) {
    this.title = title;
    this.author = author;
    this.nsid = nsid;
    this.modelId = modelId;
    this.lores = lores;
  }

  public ItemStack makeDisc() {
    // Use Cat music disc as base
    ItemStack disc = new ItemStack(Material.MUSIC_DISC_CAT);

    ArrayList<String> lores = new ArrayList<>();

    // Custom disc use a special specifier for recognition
    lores.add(SPECIFIER);
    lores.add(ChatColor.GOLD + "Author: " + ChatColor.GREEN + author);
    lores.add(nsidEncoder(nsid));
    lores.addAll(this.lores);

    ItemMeta meta = disc.getItemMeta();
    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    meta.setDisplayName(ChatColor.GOLD + title);
    meta.setCustomModelData(modelId);
    meta.setLore(lores);

    disc.setItemMeta(meta);
    return disc;
  }

  private String nsidEncoder(String nsid) {
    String enc = "";
    for (Character c : nsid.toCharArray()) {
      enc += ChatColor.COLOR_CHAR + c.toString();
    }
    return enc;
  }

}