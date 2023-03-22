package me.spartacus04.jext.jukebox

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class JukeboxPersistentDataContainer(jukeboxContainer: JukeboxContainer) {
    private val containers = arrayListOf(jukeboxContainer)
    private val id = jukeboxContainer.id
    private val plugin = jukeboxContainer.plugin

    private var location : Location? = null
    private var playing : JukeboxEntry? = null
    var slot = -1

    private fun refresh() {
        containers.forEach {
            it.refresh()
        }
    }

    fun addDisc(disc: DiscContainer, slot: Int) {
        addDisc(JukeboxEntry("jext", disc.namespace), slot)
    }

    fun addDisc(itemStack: ItemStack, slot: Int) {
        addDisc(JukeboxEntry("minecraft", itemStack.type.name), slot)
    }

    private fun addDisc(disc: JukeboxEntry, slot: Int) {
        if(!loadedData.containsKey(id)) {
            loadedData[id] = HashMap()
        }

        loadedData[id]!![slot] = disc

        refresh()
        save(plugin)
    }

    fun removeDisc(slot: Int) {
        if (!loadedData.containsKey(id)) return

        loadedData[id]!!.remove(slot)

        refresh()
        save(plugin)
    }

    fun playDisc(slot: Int, location: Location) {
        if(!loadedData.containsKey(id)) return
        if(!loadedData[id]!!.containsKey(slot)) return


        if (this.slot != -1) {
            stopPlaying()
        }

        this.location = location
        this.slot = slot

        refresh()

        val duration = loadedData[id]!![slot]!!.play(location, plugin)
        playing = loadedData[id]!![slot]!!

        if(duration.toInt() == 0) return

        // FIXME: Music stopping earlier than it should
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { stopPlaying() }, duration * 20)
    }

    fun stopPlaying() {
        if (playing == null || location == null) return

        playing!!.stop(location!!, plugin)

        slot = -1
        location = null

        refresh()
    }

    fun getDiscs() : HashMap<Int, JukeboxEntry> {
        if(!loadedData.containsKey(id)) {
            return HashMap()
        }

        return loadedData[id]!!
    }

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()
        private val loadedData = HashMap<String, HashMap<Int, JukeboxEntry>>()
        private val instances = ArrayList<JukeboxPersistentDataContainer>()

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

        fun get(jukeboxContainer: JukeboxContainer): JukeboxPersistentDataContainer {
            fixer(jukeboxContainer)

            val instance = instances.find { it.id == jukeboxContainer.id }

            return if(instance != null) {
                instance.containers.add(jukeboxContainer)
                instance
            } else {
                val newInstance = JukeboxPersistentDataContainer(jukeboxContainer)
                instances.add(newInstance)
                newInstance
            }
        }

        fun remove(jukeboxContainer: JukeboxContainer) {
            val instance = instances.find { it.containers.contains(jukeboxContainer) } ?: return

            instance.containers.remove(jukeboxContainer)
        }

        fun save(plugin: JavaPlugin) {
            val file = plugin.dataFolder.resolve(".savedata")

            if(!file.exists()) {
                file.createNewFile()
            }

            file.writeText(gson.toJson(loadedData))
        }

        fun breakJukebox(id: String) {
            val instance = instances.find { it.id == id }

            loadedData.remove(id)

            instance?.stopPlaying()
            instance?.containers?.forEach {
                it.close()
            }
        }

        // Fixes a bug where two or more jukeboxes with the same id are loaded
        private fun fixer(jukeboxContainer: JukeboxContainer) {
            val id = "${jukeboxContainer.location.world!!.name}${jukeboxContainer.location.blockX}${jukeboxContainer.location.blockY}${jukeboxContainer.location.blockZ}"

            if(loadedData.containsKey(id)) {
                loadedData[jukeboxContainer.id] = loadedData[id]!!
                loadedData.remove(id)

                save(jukeboxContainer.plugin)
            }
        }
    }
}