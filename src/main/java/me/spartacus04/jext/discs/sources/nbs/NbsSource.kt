package me.spartacus04.jext.discs.sources.nbs

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.Bukkit

internal class NbsSource : DiscSource {
    private val nbsTypeToken = object : TypeToken<ArrayList<NbsDisc>>() {}.type
    private val baseNbsDir = PLUGIN.dataFolder.resolve("nbs")

    private fun isNoteBlockApiPresent(): Boolean {
        return Bukkit.getPluginManager().getPlugin("NoteBlockAPI") != null
    }

    override suspend fun getDiscs(): List<Disc> {
        if (!isNoteBlockApiPresent()) {
            return emptyList()
        }

        if(!baseNbsDir.exists()) {
            baseNbsDir.mkdirs()
        }

        val contents = ASSETS_MANAGER.getAsset("nbs")?.bufferedReader()?.readText() ?: "[]"

        val discsMeta = GSON.fromJson<ArrayList<NbsDisc>>(contents, nbsTypeToken)

        // rename all nbs files to lowercase to avoid issues

        baseNbsDir.listFiles { _, name -> name.endsWith(".nbs") }?.forEach { nbsFile ->
            val newName = nbsFile.name.lowercase()
            nbsFile.renameTo(baseNbsDir.resolve(newName))
        }

        val nbsFiles = baseNbsDir.listFiles { _, name -> name.endsWith(".nbs") } ?: emptyArray()

        // Remove discs that don't have a corresponding NBS file and add new discs when needed

        var changes = false

        nbsFiles.forEach { nbsFile ->
            val discMeta = discsMeta.find { it.DISC_NAMESPACE == nbsFile.nameWithoutExtension }

            if(discMeta == null) {
                changes = true

                val song = NBSDecoder.parse(nbsFile)

                val newDiscMeta = NbsDisc(
                    TITLE = song.title.ifBlank { nbsFile.nameWithoutExtension },
                    AUTHOR = song.originalAuthor,
                    DISC_NAMESPACE = nbsFile.nameWithoutExtension,
                    MODEL_DATA = 0,
                    LORE = song.description.split("\n").filter { it.isNotBlank() }
                )

                discsMeta.add(newDiscMeta)
            }
        }

        discsMeta.removeIf { discMeta ->
            val nbsFile = baseNbsDir.resolve("${discMeta.DISC_NAMESPACE}.nbs")

            if(!nbsFile.exists()) {
                changes = true
                return@removeIf true
            }

            return@removeIf false
        }

        if(changes) {
            ASSETS_MANAGER.saveAsset("nbs", GSON.toJson(discsMeta))
        }

        return discsMeta.mapNotNull { it.toJextDisc() }
    }
}