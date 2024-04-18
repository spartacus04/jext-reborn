package me.spartacus04.jext.discs.sources.nbs

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource
import org.bukkit.Bukkit

class NbsSource : DiscSource{
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    private fun isNoteBlockApiPresent(): Boolean {
        return Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")
    }

    override suspend fun getDiscs(): List<Disc> {
        if (!isNoteBlockApiPresent()) {
            return emptyList()
        }

        val baseNbsDir = PLUGIN.dataFolder.resolve("nbs")

        if(!baseNbsDir.exists()) {
            baseNbsDir.mkdirs()
        }

        val nbsTypeToken = object : TypeToken<List<NbsDisc>>() {}.type

        val contents = ASSETS_MANAGER.getFile("nbs")?.bufferedReader()?.readText() ?: return emptyList()

        return gson.fromJson<List<NbsDisc>>(contents, nbsTypeToken).mapNotNull { it.toJextDisc() }
    }

}