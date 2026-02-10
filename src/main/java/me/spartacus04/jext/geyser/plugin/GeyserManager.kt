package me.spartacus04.jext.geyser.plugin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spartacus04.jext.Jext
import me.spartacus04.jext.language.DefaultMessages.GEYSER_RELOAD
import org.bukkit.entity.Player
import java.util.UUID

internal class GeyserManager(val plugin: Jext) {
    private var geyser: GeyserMode? = null
    private var scope = CoroutineScope(Dispatchers.IO)
    private val memoizedPlayers = HashMap<UUID, Boolean>()

    init {
        plugin.scheduler.runTaskAsynchronously {
            scope.launch {
                geyser = if(plugin.config.GEYSER_STANDALONE_IP_PORT.isNotBlank()) {
                    GeyserStandalone(plugin.config.GEYSER_STANDALONE_IP_PORT)
                } else {
                    try {
                        GeyserSpigot(plugin)
                    } catch (_: NoClassDefFoundError) {
                        null
                    }
                }
            }
        }
    }

    fun reloadGeyser() {
        plugin.scheduler.runTaskAsynchronously {
            memoizedPlayers.clear()
            geyser?.close()

            scope.launch {
                geyser = if(plugin.config.GEYSER_STANDALONE_IP_PORT.isNotBlank()) {
                    GeyserStandalone(plugin.config.GEYSER_STANDALONE_IP_PORT)
                } else {
                    try {
                        GeyserSpigot(plugin)
                    } catch (_: NoClassDefFoundError) {
                        null
                    }
                }

                plugin.colosseumLogger.info(GEYSER_RELOAD)
            }
        }

    }

    fun isBedrockPlayer(player: Player) : Boolean {
        if(player.uniqueId in memoizedPlayers) {
            return memoizedPlayers[player.uniqueId]!!
        }

        val result = geyser?.isBedrockPlayer(player.uniqueId) ?: false

        memoizedPlayers[player.uniqueId] = result
        return result
    }

    fun applyResourcePack(buffer: ByteArray) {
        geyser?.applyResourcePack(buffer)
    }
}