package me.tajam.jext;

import java.util.ArrayList;
import java.util.HashMap;

import me.tajam.jext.config.ConfigDiscData;
import me.tajam.jext.config.ConfigDiscData.Path;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DiscContainer {

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
    if (SpigotVersion.isVersion1_16())
      SOUND_MAP.put(Material.MUSIC_DISC_PIGSTEP, Sound.MUSIC_DISC_PIGSTEP);
  }
  public static final Material BASEDISC_MATERIAL = Material.MUSIC_DISC_11;

  private String title;
  private String author;
  private String namespaceID;
  private int customModelData;
  private boolean creeperDrop;
  private ArrayList<String> lores;
  private Material material;

  public DiscContainer(ConfigDiscData configDiscData) {
    this.title = configDiscData.getName();
    this.author = configDiscData.getStringData(Path.AUTHOR);
    this.namespaceID = configDiscData.getStringData(Path.NAMESPACE);
    this.customModelData = configDiscData.getIntegerData(Path.MODEL_DATA);
    this.creeperDrop = configDiscData.getBooleanData(Path.CREEPER_DROP);
    this.lores = configDiscData.getLores();
    this.material = BASEDISC_MATERIAL;
  }

  public DiscContainer(ItemStack disc) throws IllegalStateException {
    if (isCustomDisc(disc)) {
      this.material = disc.getType();
      final ItemMeta meta = disc.getItemMeta();
      this.customModelData = meta.getCustomModelData();
      final ArrayList<String> itemLores = new ArrayList<>(meta.getLore());
      this.lores = itemLores;
      final DiscPersistentDataContainer helper = new DiscPersistentDataContainer(meta);
      this.author = helper.getAuthor();
      this.namespaceID = helper.getNamespaceID();
      this.title = helper.getTitle();
    } else {
      throw new IllegalStateException("Custom disc identifier missing!");
    }
  }

  public ItemStack getDiscItem() {
    final ItemStack disc = new ItemStack(material);
    final ItemMeta meta = disc.getItemMeta();
    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
    meta.setCustomModelData(customModelData);

    // Store custom disc data
    final DiscPersistentDataContainer helper = new DiscPersistentDataContainer(meta);
    helper.setAuthor(this.author);
    helper.setNamespaceID(this.namespaceID);
    helper.setTitle(this.title);
    helper.setIdentifier();

    // Lores and disc info
    final ArrayList<String> lores = new ArrayList<>();
    lores.add(new Log().gr(this.author).gr(" - ").gr(this.title).text());
    lores.addAll(this.lores);
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

  public boolean canCreeperDrop() {
    return creeperDrop;
  }

  @Override
  public String toString() {
    return title;
  }

  private boolean isCustomDisc(final ItemStack disc) {
    if (disc == null || !disc.hasItemMeta()) {
      return false;
    }
    final ItemMeta meta = disc.getItemMeta();
    final DiscPersistentDataContainer helper = new DiscPersistentDataContainer(meta);
    try {
      if (!helper.checkIdentifier()) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}