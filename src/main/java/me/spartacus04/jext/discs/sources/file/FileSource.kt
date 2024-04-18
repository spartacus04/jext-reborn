package me.spartacus04.jext.discs.sources.file

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource

internal class FileSource : DiscSource {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    override suspend fun getDiscs(): List<Disc> {
        val discTypeToken = object : TypeToken<List<FileDisc>>() {}.type

        val contents = ASSETS_MANAGER.getDefaultFile()?.bufferedReader()?.readText() ?: return emptyList()

        return gson.fromJson<List<FileDisc>>(contents, discTypeToken).map { it.toJextDisc() }
    }
}