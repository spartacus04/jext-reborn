package me.tajam.jext;

import java.util.ArrayList;
import java.util.HashMap;

import me.tajam.jext.config.ConfigDiscData;
import me.tajam.jext.config.ConfigDiscData.Path;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

  public static final HashMap<Material, Sound> SOUND_MAP;
  static {
    SOUND_MAP = new HashMap<>();
    SOUND_MAP.put(Material.MUSIC_DISC_11, Sound.MUSIC_DISC_11);
    SOUND_MAP.put(Material.MUSIC_DISC_13, Sound.MUSIC_DISC_13);
    SOUND_MAP.put(Material.MUSIC_DISC_BLOCKS, Sound.MUSIC_DISC_BLOCKS);
    SOUND_MAP.put(Material.MUSIC_DISC_CAT, Sound.MUSIC_DISC_CAT);
    SOUND_MAP.put(Material.MUSIC_DISC_CHIRP, Sound.MUSIC_DISC_CHIRP);
    SOUND_MAP.put(Material.MUSIC_DISC_FAR, Sound.MUSIC_DISC_FAR);
    SOUND_MAP.put(Material.MUSIC_DISC_MALL, Sound.MUSIC_DISC_MALL);
    SOUND_MAP.put(Material.MUSIC_DISC_MELLOHI, Sound.MUSIC_DISC_MELLOHI);
    SOUND_MAP.put(Material.MUSIC_DISC_STAL, Sound.MUSIC_DISC_STAL);
    SOUND_MAP.put(Material.MUSIC_DISC_STRAD, Sound.MUSIC_DISC_STRAD);
    SOUND_MAP.put(Material.MUSIC_DISC_WAIT, Sound.MUSIC_DISC_WAIT);
    SOUND_MAP.put(Material.MUSIC_DISC_WARD, Sound.MUSIC_DISC_WARD);
  }
  public static final Material BASEDISC_MATERIAL = Material.MUSIC_DISC_11;

  private String title;
  private String author;
  private String namespaceID;
  private int customModelData;
  private ArrayList<String> lores;
  private Material material;

  public DiscContainer(ConfigDiscData discData) {
    this.title = discData.getName();
    this.author = discData.getStringData(Path.AUTHOR);
    this.namespaceID = discData.getStringData(Path.NAMESPACE);
    this.customModelData = discData.getIntegerData(Path.MODEL_DATA);
    this.lores = discData.getLores();
    this.material = BASEDISC_MATERIAL;
  }

  public DiscContainer(ItemStack disc) throws IllegalStateException {
    if (isCustomDisc(disc)) {
      this.material = disc.getType();
      final ItemMeta meta = disc.getItemMeta();
      this.title = meta.getDisplayName();
      this.customModelData = meta.getCustomModelData();

      final ArrayList<String> itemLores = new ArrayList<>(meta.getLore());
      this.author = itemLores.get(1).substring(AUTHOR_CAPTION.length() + 1, itemLores.get(1).length());
      this.namespaceID = namespaceIDDecode(itemLores.get(2));
      final ArrayList<String> lores = new ArrayList<>(itemLores.subList(3, itemLores.size()));
      this.lores = lores;
    } else {
      throw new IllegalStateException("Custom disc identifier missing!");
    }
  }

  public ItemStack getDiscItem() {
    final ItemStack disc = new ItemStack(material);
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

  public String getAuthor() {
    return author;
  }

  public Material getMaterial() {
    return material;
  }

  @Override
  public String toString() {
    return title;
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
    if (disc == null || !disc.hasItemMeta()) {
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