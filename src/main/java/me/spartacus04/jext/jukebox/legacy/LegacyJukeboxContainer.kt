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

    /**
     * The function checks if the loadedData map contains a specific id, and if not, it adds a new HashMap to it. Then, it
     * calls the refresh function.
     */
    private fun mergedConstructor() {
        if(!loadedData.containsKey(id)) {
            loadedData[id] = HashMap()
        }

        refresh()
    }

    /**
     * The function "open" opens the inventory for a player and adds an enchantment and lore to a specific item slot if it
     * exists.
     *
     * @param player The "player" parameter is of type HumanEntity, which represents a player in the game.
     */
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

    /**
     * The function creates an array of ItemStacks, populates it with data from a loadedData map, and applies enchantments
     * and lore to the item in the playingSlot.
     *
     * @return The function `createContents()` returns an array of `ItemStack?`.
     */
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

    /**
     * The function "refresh" updates the contents of the inventory.
     */
    fun refresh() {
        inventory.contents = createContents()
    }

    /**
     * The function "close" closes the inventory for all viewers.
     */
    private fun close() {
        inventory.viewers.forEach {
            it.closeInventory()
        }
    }

    /**
     * The function "breakJukebox" drops all the items from the jukebox's contents into the world and then closes the
     * jukebox.
     */
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

        /**
         * The function `get` returns a `LegacyJukeboxContainer` object based on the provided location, and handles cases
         * where multiple jukeboxes with the same ID are loaded.
         *
         * @param plugin The `plugin` parameter is an instance of the `JavaPlugin` class. It represents the plugin that is
         * calling this `get` function.
         * @param loc The `loc` parameter is of type `Location` and represents the location of a jukebox block in the game
         * world. It contains information such as the world name, block coordinates (x, y, z), and other properties related
         * to the location.
         * @return a `LegacyJukeboxContainer` object.
         */
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

        /**
         * The function returns a LegacyJukeboxContainer object associated with a player's unique ID, creating a new one if
         * it doesn't already exist.
         *
         * @param plugin The "plugin" parameter is of type JavaPlugin, which is a class representing a plugin in the Bukkit
         * API. It is used to access various plugin-related functionalities.
         * @param player The "player" parameter is of type Player and represents the player for whom the
         * LegacyJukeboxContainer is being retrieved.
         * @return The method is returning a LegacyJukeboxContainer object.
         */
        fun get(plugin: JavaPlugin, player: Player) : LegacyJukeboxContainer {
            val id = player.uniqueId.toString()

            if(containers.containsKey(id)) {
                return containers[id]!!
            }

            val container = LegacyJukeboxContainer(plugin, player)
            containers[id] = container

            return container
        }

        /**
         * The function "reload" reads data from a file and populates a HashMap with the loaded data, either in a new
         * format or a legacy format.
         *
         * @param plugin The parameter `plugin` is of type `JavaPlugin`. It is an instance of a Java plugin that is being
         * reloaded.
         */
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

        /**
         * The function saves the loaded data to a file in the plugin's data folder using Gson.
         *
         * @param plugin The `plugin` parameter is of type `JavaPlugin`. It represents the plugin instance that is calling
         * the `save` function.
         */
        fun save(plugin: JavaPlugin) {
            val file = plugin.dataFolder.resolve(".savedata")

            if(!file.exists()) {
                file.createNewFile()
            }

            file.writeText(gson.toJson(loadedData))
        }
    }
}