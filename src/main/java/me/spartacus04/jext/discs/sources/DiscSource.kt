package me.spartacus04.jext.discs.sources

import me.spartacus04.jext.discs.Disc

/**
 * The interface `DiscSource` is used to define the methods used to get discs.
 */
interface DiscSource {

    /**
     * Gets the discs.
     * 
     * @return The list of discs.
     */
    suspend fun getDiscs(): List<Disc>
}