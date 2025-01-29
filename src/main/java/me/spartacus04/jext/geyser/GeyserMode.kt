package me.spartacus04.jext.geyser

import java.util.UUID

internal interface GeyserMode {
    fun isBedrockPlayer(player: UUID): Boolean

    fun applyResourcePack(buffer: ByteArray)
}