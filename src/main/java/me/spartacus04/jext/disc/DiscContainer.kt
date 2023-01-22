package me.spartacus04.jext.disc

import me.spartacus04.jext.Log
import me.spartacus04.jext.SpigotVersion
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
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

    var duration: Int = -1
        private set


    private var customModelData = 0
    private var creeperDrop = false
    private var lores: ArrayList<String>


    val material: Material = Material.MUSIC_DISC_11

    constructor(data: Disc) {
        title = data.TITLE
        author = data.AUTHOR
        namespace = data.DISC_NAMESPACE
        customModelData = data.MODEL_DATA
        creeperDrop = data.CREEPER_DROP
        lores = data.LORE.toCollection(ArrayList())
        duration = data.DURATION
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

            duration = DISCS.find { it.DISC_NAMESPACE == namespace }!!.DURATION
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

    private fun isCustomDisc(disc: ItemStack): Boolean {
        if (!disc.hasItemMeta()) {
            return false
        }

        val meta = disc.itemMeta
        val helper = DiscPersistentDataContainer(meta)

        try {
            if (!helper.checkIdentifier()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }

    companion object {
        val SOUND_MAP = HashMap<Material, SoundData>()

        init {
            SOUND_MAP[Material.MUSIC_DISC_11] = SoundData(Sound.MUSIC_DISC_11, 72)
            SOUND_MAP[Material.MUSIC_DISC_13] = SoundData(Sound.MUSIC_DISC_13, 179)
            SOUND_MAP[Material.MUSIC_DISC_BLOCKS] = SoundData(Sound.MUSIC_DISC_BLOCKS, 346)
            SOUND_MAP[Material.MUSIC_DISC_CAT] = SoundData(Sound.MUSIC_DISC_CAT, 186)
            SOUND_MAP[Material.MUSIC_DISC_CHIRP] = SoundData(Sound.MUSIC_DISC_CHIRP, 186)
            SOUND_MAP[Material.MUSIC_DISC_FAR] = SoundData(Sound.MUSIC_DISC_FAR, 175)
            SOUND_MAP[Material.MUSIC_DISC_MALL] = SoundData(Sound.MUSIC_DISC_MALL, 198)
            SOUND_MAP[Material.MUSIC_DISC_MELLOHI] = SoundData(Sound.MUSIC_DISC_MELLOHI, 97)
            SOUND_MAP[Material.MUSIC_DISC_STAL] = SoundData(Sound.MUSIC_DISC_STAL, 151)
            SOUND_MAP[Material.MUSIC_DISC_STRAD] = SoundData(Sound.MUSIC_DISC_STRAD, 189)
            SOUND_MAP[Material.MUSIC_DISC_WAIT] = SoundData(Sound.MUSIC_DISC_WAIT, 238)
            SOUND_MAP[Material.MUSIC_DISC_WARD] = SoundData(Sound.MUSIC_DISC_WARD, 252)

            if(SpigotVersion.VERSION >= 16) {
                SOUND_MAP[Material.MUSIC_DISC_PIGSTEP] = SoundData(Sound.MUSIC_DISC_PIGSTEP, 149)
            }

            if(SpigotVersion.VERSION >= 18) {
                SOUND_MAP[Material.MUSIC_DISC_OTHERSIDE] = SoundData(Sound.MUSIC_DISC_OTHERSIDE, 196)
            }

            if(SpigotVersion.VERSION >= 19) {
                SOUND_MAP[Material.MUSIC_DISC_5] = SoundData(Sound.MUSIC_DISC_5, 179)
            }
        }

        data class SoundData(val sound: Sound, val duration: Int)
    }
}