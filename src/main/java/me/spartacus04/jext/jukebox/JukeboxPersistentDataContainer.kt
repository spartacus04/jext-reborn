package me.spartacus04.jext.jukebox

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.UUID
import java.util.stream.Collectors
import kotlin.collections.HashMap

class JukeboxPersistentDataContainer {
    private var playingLocation: Location? = null
    var playingDisc: Int = -1
    var discs: HashMap<Int, String> = HashMap()

    @Transient
    val subscribed = HashMap<UUID, () -> Unit>()
    val unsubscribedFuncs = HashMap<UUID, () -> Unit>()

    fun addDisc(disc: DiscContainer, slot: Int) {
        discs[slot] = disc.namespace

        notifySubscribers()
        JukeboxPersistentDataContainerManager.save()
    }

    fun addDisc(itemStack: ItemStack, slot: Int) {
        discs[slot] = itemStack.type.name

        notifySubscribers()
        JukeboxPersistentDataContainerManager.save()
    }

    fun playDisc(slot: Int, location: Location) {
        if(playingDisc != -1) {
            stopPlaying()
        }

        playingLocation = location
        playingDisc = slot

        if(JukeboxPersistentDataContainerManager.isDiscContainer(discs[slot]!!)) {
            val disc = DISCS.first { it.DISC_NAMESPACE == discs[slot] }
            DiscPlayer(DiscContainer(disc)).play(location)
        } else {
            val type = Material.matchMaterial(discs[slot] as String)
            if(type != null && type.isRecord) {
                location.world!!.playSound(location, DiscContainer.SOUND_MAP[type]!!, 1f, 1f)
            }
        }

        notifySubscribers()
    }

    fun stopPlaying() {
        playingLocation?.let {
            discs[playingDisc]?.let { disc ->
                if(JukeboxPersistentDataContainerManager.isDiscContainer(disc)) {
                    val dsc = DISCS.first { it.DISC_NAMESPACE == disc }
                    DiscPlayer(DiscContainer(dsc)).stop(it)
                } else {
                    // stop vanilla record
                    it.world!!.players.forEach { player ->
                        player.stopSound(DiscContainer.SOUND_MAP[Material.matchMaterial(disc)!!]!!)
                    }
                }
            }
        }

        playingLocation = null
        playingDisc = -1
        notifySubscribers()
    }

    fun removeDisc(slot: Int) {
        discs.remove(slot)

        notifySubscribers()
        JukeboxPersistentDataContainerManager.save()
    }

    fun subscribe(self: UUID, f: () -> Unit) {
        subscribed[self] = f
    }

    private fun notifySubscribers() {
        subscribed.forEach { it.value() }
    }

    fun unsubscribe(self: UUID) {
        unsubscribedFuncs[self]?.invoke()
        unsubscribedFuncs.remove(self)
        subscribed.remove(self)
    }
}

class JukeboxPersistentDataContainerManager {
    companion object {
        var jukeboxContainers = HashMap<String, JukeboxPersistentDataContainer>()
        private lateinit var file: File
        private lateinit var gson: Gson

        fun init(plugin: JavaPlugin) {
            // load data from file
            gson = GsonBuilder().setPrettyPrinting().setLenient().create()
            file = plugin.dataFolder.resolve(".savedata")

            if(!file.exists()) {
                file.createNewFile()
            } else {
                val json = file.readText()
                val data = gson.fromJson(json, object: TypeToken <HashMap<String, HashMap<Int, String>>>() {}.type) as HashMap<String, HashMap<Int, String>>? ?: return

                jukeboxContainers = data.mapValues { entry ->
                    val container = JukeboxPersistentDataContainer()
                    container.discs = entry.value
                    container
                } as HashMap<String, JukeboxPersistentDataContainer>
            }
        }

        fun save() {
            val cleaned = jukeboxContainers.map {
                it.key to it.value.discs
            }.filter { it.second.isNotEmpty() }.stream().collect(Collectors.toMap({ it.first }, { it.second }))


            val json = gson.toJson(cleaned, HashMap<String, JukeboxPersistentDataContainer>()::class.java)
            file.writeText(json)
        }

        fun isDiscContainer(namespace: String) : Boolean {
            return DISCS.any { it.DISC_NAMESPACE == namespace }
        }
    }
}