package me.spartacus04.jext.discs.sources.file

import com.google.common.reflect.TypeToken
import me.spartacus04.jext.Jext
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.discs.sources.DiscSource

internal class FileSource : DiscSource {
    override suspend fun getDiscs(plugin: Jext): List<Disc> {
        val discTypeToken = object : TypeToken<List<FileDisc>>() {}.type

        val contents = plugin.assetsManager.getAsset("discs")?.bufferedReader()?.readText() ?: return emptyList()

        return plugin.gson.fromJson<List<FileDisc>>(contents, discTypeToken).map { it.toJextDisc(plugin) }
    }
}