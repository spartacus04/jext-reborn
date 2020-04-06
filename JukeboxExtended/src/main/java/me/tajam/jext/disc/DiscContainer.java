package me.tajam.jext.disc;

import java.util.ArrayList;

import me.tajam.jext.config.ConfigDiscData;
import me.tajam.jext.config.ConfigDiscData.Path;
import me.tajam.jext.exception.InvalidDiscFormatException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*=====

  Disc uses lore to store information for custom disc recognition specifier string and music namespace.
  These information are encoded using Bukkit special color symbol so that they will hide in player's inventory interface
  Here is the structure:
  <0> - [Hidden] The special specifier string
  <1> - The author name
  <2> - [Hidden] The namespace of the music
  <3> - From here the rest are customized lore texts

 =====*/

public class DiscContainer {

  private static final String SPECIFIER = 
    ChatColor.COLOR_CHAR + "J" + 
    ChatColor.COLOR_CHAR + "E" + 
    ChatColor.COLOR_CHAR + "X" + 
    ChatColor.COLOR_CHAR + "T";
  private static final String AUTHOR_CAPTION = "Author: ";
  private static final Material BASEDISC_MATERIAL = Material.MUSIC_DISC_CAT;

  private String title;
  private String author;
  private String namespaceID;
  private int customModelData;
  private ArrayList<String> lores;

  public DiscContainer(String title, String author, String namespaceID, int customModelData, ArrayList<String> lores) {
    this.title = title;
    this.author = author;
    this.namespaceID = namespaceID;
    this.customModelData = customModelData;
    this.lores = lores;
  }

  public DiscContainer(ConfigDiscData discData) {
    this.title = discData.getName();
    this.author = discData.getStringData(Path.AUTHOR);
    this.namespaceID = discData.getStringData(Path.NAMESPACE);
    this.customModelData = discData.getIntegerData(Path.MODEL_DATA);
    this.lores = discData.getLores();
  }

  public DiscContainer(final ItemStack disc) throws InvalidDiscFormatException {
    if (isCustomDisc(disc)) {
      final ItemMeta meta = disc.getItemMeta();
      this.title = meta.getDisplayName();
      this.customModelData = meta.getCustomModelData();

      final ArrayList<String> itemLores = new ArrayList<>(meta.getLore());
      this.author = itemLores.get(1).substring(AUTHOR_CAPTION.length(), itemLores.get(1).length());
      this.namespaceID = namespaceIDDecode(itemLores.get(2));
      final ArrayList<String> lores = new ArrayList<>(itemLores.subList(3, itemLores.size()));
      this.lores = lores;
    } else {
      throw new InvalidDiscFormatException();
    }
  }

  public ItemStack getDiscItem() {
    final ItemStack disc = new ItemStack(BASEDISC_MATERIAL);
    final ArrayList<String> lores = new ArrayList<>();

    // Custom disc use a special specifier for recognition
    lores.add(SPECIFIER);
    lores.add(ChatColor.GOLD + AUTHOR_CAPTION + ChatColor.GREEN + author);
    lores.add(namespaceIDEncode(namespaceID));
    lores.addAll(this.lores);

    final ItemMeta meta = disc.getItemMeta();
    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    meta.setDisplayName(ChatColor.GOLD + title);
    meta.setCustomModelData(customModelData);
    meta.setLore(lores);

    disc.setItemMeta(meta);
    return disc;
  }

  public String getNamespace() {
    return namespaceID;
  }

  private String namespaceIDEncode(final String namespaceID) {
    String enc = "";
    for (Character c : namespaceID.toCharArray()) {
      enc += ChatColor.COLOR_CHAR + c.toString();
    }
    return enc;
  }

  private String namespaceIDDecode(final String namespaceID) {
    String dec = "";
    for (final Character c : namespaceID.toCharArray()) {
      if (c != ChatColor.COLOR_CHAR)
      dec += c.toString();
    }
    return dec;
  }

  private boolean isCustomDisc(final ItemStack disc) {
    if (disc == null || disc.getType() != Material.MUSIC_DISC_CAT || !disc.hasItemMeta()) {
      return false;
    }
    final ItemMeta meta = disc.getItemMeta();
    if (!meta.hasLore()) return false;
    final ArrayList<String> lores = new ArrayList<>(meta.getLore());
    try {
      // Look for disc specifier
      if (!lores.get(0).equals(SPECIFIER)) {
        System.out.println(lores.get(0));
        System.out.println(SPECIFIER);
        return false;
      }
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

}