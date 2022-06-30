package me.spartacus04.jext.disc

import me.spartacus04.jext.Log
import me.spartacus04.jext.SpigotVersion
import me.spartacus04.jext.config.Disc
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class DiscContainer {
    private var title: String

    var author: String
        private set

    var namespace: String
        private set

    private var customModelData = 0
    var creeperDrop = false
    private var lores: ArrayList<String>


    val material: Material = Material.MUSIC_DISC_11

    constructor(data: Disc) {
        title = data.TITLE
        author = data.AUTHOR
        namespace = data.DISC_NAMESPACE
        customModelData = data.MODEL_DATA
        creeperDrop = data.CREEPER_DROP
        lores = data.LORE.toCollection(ArrayList())
    }

    constructor(disc: ItemStack) {
        if (isCustomDisc(disc)) {
            val meta = disc.itemMeta
            customModelData = meta!!.customModelData

            lores = meta.lore?.let { ArrayList(it) }!!

            val helper = DiscPersistentDataContainer(meta)
            author = helper.author!!
            namespace = helper.namespaceID!!
            title = helper.title!!
        } else {
            throw IllegalStateException("Custom disc identifier missing!")
        }
    }

    // Store custom disc data
    val discItem: ItemStack
        get() {
            val disc = ItemStack(material)
            val meta = disc.itemMeta

            meta!!.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

            meta.setCustomModelData(customModelData)

            // Store custom disc data
            val helper = DiscPersistentDataContainer(meta)
            helper.author = author
            helper.namespaceID = namespace
            helper.title = title
            helper.setIdentifier()

            // Lores and disc info
            val lores = ArrayList<String?>()
            lores.add(Log().gr(author).gr(" - ").gr(title).text())
            lores.addAll(this.lores)

            meta.lore = lores
            disc.itemMeta = meta
            return disc
        }

    override fun toString(): String {
        return title
    }

    private fun isCustomDisc(disc: ItemStack?): Boolean {
        if (disc == null || !disc.hasItemMeta()) {
            return false
        }

        val meta = disc.itemMeta
        val helper = DiscPersistentDataContainer(meta)

        try {
            if (!helper.checkIdentifier()) {
                return false
            }
        } catch (e: Exception) {
            return true
        }

        return true
    }

    companion object {
        val SOUND_MAP = HashMap<Material, Sound>()

        init {
            SOUND_MAP[Material.MUSIC_DISC_11] = Sound.MUSIC_DISC_11
            SOUND_MAP[Material.MUSIC_DISC_13] = Sound.MUSIC_DISC_13
            SOUND_MAP[Material.MUSIC_DISC_BLOCKS] = Sound.MUSIC_DISC_BLOCKS
            SOUND_MAP[Material.MUSIC_DISC_CAT] = Sound.MUSIC_DISC_CAT
            SOUND_MAP[Material.MUSIC_DISC_CHIRP] = Sound.MUSIC_DISC_CHIRP
            SOUND_MAP[Material.MUSIC_DISC_FAR] = Sound.MUSIC_DISC_FAR
            SOUND_MAP[Material.MUSIC_DISC_MALL] = Sound.MUSIC_DISC_MALL
            SOUND_MAP[Material.MUSIC_DISC_MELLOHI] = Sound.MUSIC_DISC_MELLOHI
            SOUND_MAP[Material.MUSIC_DISC_STAL] = Sound.MUSIC_DISC_STAL
            SOUND_MAP[Material.MUSIC_DISC_STRAD] = Sound.MUSIC_DISC_STRAD
            SOUND_MAP[Material.MUSIC_DISC_WAIT] = Sound.MUSIC_DISC_WAIT
            SOUND_MAP[Material.MUSIC_DISC_WARD] = Sound.MUSIC_DISC_WARD

            if(SpigotVersion.VERSION >= 16) {
                SOUND_MAP[Material.MUSIC_DISC_PIGSTEP] = Sound.MUSIC_DISC_PIGSTEP
            }

            if(SpigotVersion.VERSION >= 18) {
                SOUND_MAP[Material.MUSIC_DISC_OTHERSIDE] = Sound.MUSIC_DISC_OTHERSIDE
            }

            if(SpigotVersion.VERSION >= 19) {
                SOUND_MAP[Material.MUSIC_DISC_5] = Sound.MUSIC_DISC_5
            }
        }
    }
}