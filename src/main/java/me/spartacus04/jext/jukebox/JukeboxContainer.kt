package me.spartacus04.jext.jukebox

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

class JukeboxContainer {
    private val id: String
    var location: Location
    val plugin: JavaPlugin
    val inventory = Bukkit.createInventory(null, 54, LANG.format("en_us", "jukebox", true))

    var playingLocation: Location? = null
    var playingSlot = -1
    var playing: JukeboxEntry? = null

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
                        add(LANG.format("en_us", "playing", true))
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
                        add(LANG.format("en_us", "playing", true))
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

        val loadedData = HashMap<String, HashMap<Int, JukeboxEntry>>()
        val containers = HashMap<String, JukeboxContainer>()

        fun get(plugin: JavaPlugin, loc: Location) : JukeboxContainer {
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

            val container = JukeboxContainer(plugin, loc)
            containers[id] = container

            return container
        }

        fun get(plugin: JavaPlugin, player: Player) : JukeboxContainer {
            val id = player.uniqueId.toString()

            if(containers.containsKey(id)) {
                return containers[id]!!
            }

            val container = JukeboxContainer(plugin, player)
            containers[id] = container

            return container
        }

        fun reload(plugin: JavaPlugin) {
            val legacyTypeToken = object: TypeToken<HashMap<String, HashMap<Int, String>>>() {}.type
            val typeToken = object: TypeToken<HashMap<String, HashMap<Int, JukeboxEntry>>>() {}.type
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
                    val data = gson.fromJson<HashMap<String, HashMap<Int, JukeboxEntry>>>(text, typeToken)

                    if(data != null) {
                        loadedData.putAll(data)
                    }
                } catch (_: JsonSyntaxException) {
                    // legacy format

                    val data = gson.fromJson<HashMap<String, HashMap<Int, String>>>(text, legacyTypeToken)

                    loadedData.putAll(
                        data.mapValues { entry ->
                            entry.value.mapValues { JukeboxEntry.fromLegacyString(it.value) }.toMap(HashMap())
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