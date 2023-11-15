package me.spartacus04.jext.discs.sources

import me.spartacus04.jext.discs.Disc

interface DiscSource {
    suspend fun getDiscs(): List<Disc>
}