package me.spartacus04.jext.geyser

import me.spartacus04.jext.language.LanguageManager.Companion.GEYSER_RELOAD
import org.bukkit.Bukkit
import org.bukkit.entity.Player

internal class GeyserManager {
    private var geyser: GeyserMode?

    init {
        geyser = try {
            GeyserSpigot()
        } catch (_ : NoClassDefFoundError) {
            try {
                GeyserStandalone()
            } catch (_ : IllegalStateException) {
                null
            }
        }
    }

    fun reloadGeyser() {
        geyser = try {
            GeyserSpigot()
        } catch (_ : NoClassDefFoundError) {
            try {
                GeyserStandalone()
            } catch (_ : IllegalStateException) {
                null
            }
        }

        Bukkit.getConsoleSender().sendMessage(GEYSER_RELOAD)
    }

    fun isBedrockPlayer(player: Player) : Boolean {
        return geyser?.isBedrockPlayer(player.uniqueId) ?: false
    }

    fun applyResourcePack(buffer: ByteArray) {
        geyser?.applyResourcePack(buffer)
    }
}