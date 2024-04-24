package me.spartacus04.jext.discs.sources.file

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource

internal class FileSource : DiscSource {
    override suspend fun getDiscs(): List<Disc> {
        val discTypeToken = object : TypeToken<List<FileDisc>>() {}.type

        val contents = ASSETS_MANAGER.getAsset("discs")?.bufferedReader()?.readText() ?: return emptyList()

        return GSON.fromJson<List<FileDisc>>(contents, discTypeToken).map { it.toJextDisc() }
    }
}