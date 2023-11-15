package me.spartacus04.jext.utils

import me.spartacus04.jext.State.VERSION
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTables


object Constants {
    /**
     * A data class that holds a Sound and a duration
     *
     * @property sound The sound
     * @property duration The duration
     * @constructor Creates a SoundData
     */
    data class SoundData(val sound: Sound, val duration: Int)

    /**
     * A data class that holds an itemstack and a chance varying from 1 to 1000
     *
     * @property chance The chance
     * @property stack The itemstack
     * @constructor Creates a ChanceStack
     */
    data class ChanceStack(val chance: Int, val stack: ItemStack)

    val SOUND_MAP = HashMap<Material, SoundData>()

    val FRAGMENT_LIST: ArrayList<Material> = run {
        val frags = ArrayList<Material>()

        if(VERSION >= "1.19") {
            frags.add(Material.DISC_FRAGMENT_5)
        }

        return@run frags
    }

    val DEFAULT_DISCS_LOOT_TABLE = hashMapOf(
        LootTables.SIMPLE_DUNGEON.key.key to arrayListOf(
            ChanceStack(215, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(215, ItemStack(Material.MUSIC_DISC_CAT)),
        ),
        LootTables.WOODLAND_MANSION.key.key to arrayListOf(
            ChanceStack(218, ItemStack(Material.MUSIC_DISC_13)),
            ChanceStack(218, ItemStack(Material.MUSIC_DISC_CAT)),
        ),
    )
    val DEFAULT_FRAGMENTS_LOOT_TABLE = HashMap<String, ArrayList<ChanceStack>>()

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

        if(VERSION >= "1.16") {
            SOUND_MAP[Material.MUSIC_DISC_PIGSTEP] = SoundData(Sound.MUSIC_DISC_PIGSTEP, 149)

            DEFAULT_DISCS_LOOT_TABLE[LootTables.BASTION_TREASURE.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
            DEFAULT_DISCS_LOOT_TABLE[LootTables.BASTION_OTHER.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
            DEFAULT_DISCS_LOOT_TABLE[LootTables.BASTION_BRIDGE.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
            DEFAULT_DISCS_LOOT_TABLE[LootTables.BASTION_HOGLIN_STABLE.key.key] = arrayListOf(
                ChanceStack(56, ItemStack(Material.MUSIC_DISC_PIGSTEP)),
            )
        }

        if(VERSION >= "1.18") {
            SOUND_MAP[Material.MUSIC_DISC_OTHERSIDE] = SoundData(Sound.MUSIC_DISC_OTHERSIDE, 196)

            DEFAULT_DISCS_LOOT_TABLE[LootTables.SIMPLE_DUNGEON.key.key]!!.add(
                ChanceStack(31, ItemStack(Material.MUSIC_DISC_OTHERSIDE))
            )

            DEFAULT_DISCS_LOOT_TABLE[LootTables.STRONGHOLD_CORRIDOR.key.key] = arrayListOf(
                ChanceStack(25, ItemStack(Material.MUSIC_DISC_OTHERSIDE)),
            )
        }

        if(VERSION >= "1.19") {
            SOUND_MAP[Material.MUSIC_DISC_5] = SoundData(Sound.MUSIC_DISC_5, 179)

            DEFAULT_DISCS_LOOT_TABLE[LootTables.ANCIENT_CITY.key.key] = arrayListOf(
                ChanceStack(161, ItemStack(Material.MUSIC_DISC_13)),
                ChanceStack(161, ItemStack(Material.MUSIC_DISC_CAT)),
                ChanceStack(84, ItemStack(Material.MUSIC_DISC_OTHERSIDE)),
            )

            DEFAULT_FRAGMENTS_LOOT_TABLE[LootTables.ANCIENT_CITY.key.key] = arrayListOf(
                ChanceStack(298, ItemStack(Material.DISC_FRAGMENT_5)),
            )
        }

        if(VERSION >= "1.20") {
            SOUND_MAP[Material.MUSIC_DISC_RELIC] = SoundData(Sound.MUSIC_DISC_RELIC, 218)
        }
    }

    val JEXT_DISC_MATERIAL = Material.MUSIC_DISC_11
    val JEXT_FRAGMENT_MATERIAL = Material.DISC_FRAGMENT_5
    val JEXT_FRAGMENT_OUTPUT = Material.MUSIC_DISC_5

    val BRUSH_LOOT_TABLE_ITEMS = mapOf(
        "archaeology/trail_ruins_rare" to 12,
        "archaeology/trail_ruins_common" to 45,
        "archaeology/ocean_ruin_cold" to 15,
        "archaeology/ocean_ruin_warm" to 15,
        "archaeology/desert_pyramid" to 8,
        "archaeology/desert_well" to 8,
    )

    val CREEPER_DROPPABLE_DISCS: List<ItemStack> = listOf(
        ItemStack(Material.MUSIC_DISC_11),
        ItemStack(Material.MUSIC_DISC_13),
        ItemStack(Material.MUSIC_DISC_BLOCKS),
        ItemStack(Material.MUSIC_DISC_CAT),
        ItemStack(Material.MUSIC_DISC_CHIRP),
        ItemStack(Material.MUSIC_DISC_FAR),
        ItemStack(Material.MUSIC_DISC_MALL),
        ItemStack(Material.MUSIC_DISC_MELLOHI),
        ItemStack(Material.MUSIC_DISC_STAL),
        ItemStack(Material.MUSIC_DISC_STRAD),
        ItemStack(Material.MUSIC_DISC_WAIT),
        ItemStack(Material.MUSIC_DISC_WARD),
    )

    const val BSTATS_METRICS = 16571
}