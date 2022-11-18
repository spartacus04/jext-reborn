package me.spartacus04.jext.disc

import com.google.gson.Gson
import me.spartacus04.jext.JextNamespace
import org.bukkit.block.TileState
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class JukeboxPersistentDataContainer(meta: TileState) {
    private val id = "JEXT"
    private val gson: Gson;
    private val container: PersistentDataContainer

    init {
        container = meta.persistentDataContainer
        gson = Gson()
    }

    var discList: ArrayList<DiscContainer>
        get() {
            val data = container.get(
                JextNamespace.DISCS.get()!!,
                PersistentDataType.STRING
            ) ?: return ArrayList()

            return gson.fromJson(data, Array<DiscContainer>::class.java).toCollection(ArrayList())
        }
        set(value) {
            container.set(
                JextNamespace.DISCS.get()!!,
                PersistentDataType.STRING,
                gson.toJson(value)
            )
        }

    var currentIndex: Int
        get() = container.get(
            JextNamespace.DISCS_INDEX.get()!!,
            PersistentDataType.INTEGER
        ) ?: -1
        set(value) {
            container.set(
                JextNamespace.DISCS_INDEX.get()!!,
                PersistentDataType.INTEGER,
                value
            )
        }
}
