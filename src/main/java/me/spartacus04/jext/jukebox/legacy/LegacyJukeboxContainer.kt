package me.spartacus04.jext.jukebox.legacy

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

@Suppress("deprecation")
@ScheduledForRemoval(inVersion = "1.3")
@Deprecated("This is part of the legacy jukebox gui system. It's going to be removed in the next major update.")
class LegacyJukeboxContainer {
    private val id: String
    var location: Location
    val plugin: JavaPlugin
    val inventory = Bukkit.createInventory(null, 54, LANG["en_us", "jukebox"])

    var playingLocation: Location? = null
    var playingSlot = -1
    var playing: LegacyJukeboxEntry? = null

    private constructor(plugin: JavaPlugin, loc: Location) {
        id = "${loc.world!!.name}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
        location = loc
        this.plugin = plugin

        mergedConstructor()
    }

    private constructor(plugin: JavaPlugin, player: HumanEntity) {
        id = player.uniqueId.toString()
        location = player.location
        this.plugin = plugin

        mergedConstructor()
    }

    private fun mergedConstructor() {
        if(!loadedData.containsKey(id)) {
            loadedData[id] = HashMap()
        }

        refresh()
    }

    fun open(player: HumanEntity) {
        player.openInventory(inventory)

        if(playingSlot != -1) {
            val item = inventory.contents[playingSlot]

            if(item != null) {
                item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.MENDING, 1)

                item.itemMeta = item.itemMeta?.apply {
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)

                    lore = (lore ?: ArrayList()).apply {
                        add(LANG["en_us", "playing"])
                    }
                }

                inventory.contents[playingSlot] = item
            }
        }
    }

    fun createContents(): Array<ItemStack?> {
        val contents = arrayOfNulls<ItemStack>(54)

        val data = loadedData[id] ?: HashMap()

        data.forEach {
            contents[it.key] = it.value.getItemstack()
        }

        if(playingSlot != -1) {
            val item = contents[playingSlot]

            if(item != null) {
                item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.MENDING, 1)

                item.itemMeta = item.itemMeta?.apply {
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)

                    lore = (lore ?: ArrayList()).apply {
                        add(LANG["en_us", "playing"])
                    }
                }

                contents[playingSlot] = item
            }
        }

        return contents
    }

    fun refresh() {
        inventory.contents = createContents()
    }

    private fun close() {
        inventory.viewers.forEach {
            it.closeInventory()
        }
    }

    fun breakJukebox() {
        val contents = createContents().filterNotNull()

        contents.forEach {
            location.world!!.dropItemNaturally(location, it)
        }

        close()
    }

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()

        val loadedData = HashMap<String, HashMap<Int, LegacyJukeboxEntry>>()
        val containers = HashMap<String, LegacyJukeboxContainer>()

        fun get(plugin: JavaPlugin, loc: Location) : LegacyJukeboxContainer {
            val id = "${loc.world!!.name}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"

            if(containers.containsKey(id)) {
                return containers[id]!!
            }

            // Fixes a bug where two or more jukeboxes with the same id are loaded
            val oldId = "${loc.world!!.name}${loc.blockX}${loc.blockY}${loc.blockZ}"

            if(loadedData.containsKey(oldId)) {
                loadedData[id] = loadedData[oldId]!!
                loadedData.remove(oldId)

                save(plugin)
            }

            val container = LegacyJukeboxContainer(plugin, loc)
            containers[id] = container

            return container
        }

        fun get(plugin: JavaPlugin, player: Player) : LegacyJukeboxContainer {
            val id = player.uniqueId.toString()

            if(containers.containsKey(id)) {
                return containers[id]!!
            }

            val container = LegacyJukeboxContainer(plugin, player)
            containers[id] = container

            return container
        }

        fun reload(plugin: JavaPlugin) {
            val legacyTypeToken = object: TypeToken<HashMap<String, HashMap<Int, String>>>() {}.type
            val typeToken = object: TypeToken<HashMap<String, HashMap<Int, LegacyJukeboxEntry>>>() {}.type
            val file = plugin.dataFolder.resolve(".savedata")

            if(loadedData.isNotEmpty()) {
                save(plugin)
            }

            if(!file.exists()) {
                file.createNewFile()
            } else {
                val text = file.readText()

                try {
                    // new format
                    val data = gson.fromJson<HashMap<String, HashMap<Int, LegacyJukeboxEntry>>>(text, typeToken)

                    if(data != null) {
                        loadedData.putAll(data)
                    }
                } catch (_: JsonSyntaxException) {
                    // legacy format

                    val data = gson.fromJson<HashMap<String, HashMap<Int, String>>>(text, legacyTypeToken)

                    loadedData.putAll(
                        data.mapValues { entry ->
                            entry.value.mapValues { LegacyJukeboxEntry.fromLegacyString(it.value) }.toMap(HashMap())
                        }
                    )
                }
            }
        }

        fun save(plugin: JavaPlugin) {
            val file = plugin.dataFolder.resolve(".savedata")

            if(!file.exists()) {
                file.createNewFile()
            }

            file.writeText(gson.toJson(loadedData))
        }
    }
}