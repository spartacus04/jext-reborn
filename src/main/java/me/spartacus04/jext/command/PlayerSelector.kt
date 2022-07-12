package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

internal class PlayerSelector(private val sender: CommandSender, private val selector: String) {
    private enum class Selector {
        ALL, WORLD, WORLD_RANDOM, RANDOM, SELF, PLAYER
    }

    private var mode: Selector?

    init {
        mode = SELECTOR_SYMBOL_MAP[selector]

        if (mode == null) {
            mode = Selector.PLAYER
        }
    }

    val players: List<Player?>?
        get() = try {
            when (mode) {
                Selector.ALL -> allPlayers
                Selector.WORLD -> worldPlayers
                Selector.WORLD_RANDOM -> getRandomPlayers(worldPlayers)
                Selector.RANDOM -> getRandomPlayers(allPlayers)
                Selector.SELF -> self
                Selector.PLAYER -> specificPlayer
                else -> null
            }
        } catch (e: IllegalStateException) {
            Log().eror().t("Invalid selector for console!").send(sender)
            null
        }

    private val allPlayers: List<Player?>
        get() = ArrayList(Bukkit.getOnlinePlayers())

    @get:Throws(IllegalStateException::class)
    private val worldPlayers: List<Player?>
        get() {
            if (sender is Player) {
                return sender.world.players
            }

            throw IllegalStateException()
        }

    private val specificPlayer: List<Player?>?
        get() {
            val players: MutableList<Player?> = ArrayList()
            val player = Bukkit.getPlayer(selector)

            if (player == null) {
                sender.sendMessage(
                    "[§aJEXT§f] ${LANG.CANNOT_FIND_PLAYER}"
                        .replace("%player%", selector)
                )
                return null
            }

            players.add(player)
            return players
        }

    private fun getRandomPlayers(players: List<Player?>): List<Player?> {
        Collections.shuffle(players)
        return players.subList(0, 1)
    }

    @get:Throws(IllegalStateException::class)
    private val self: List<Player?>
        get() {
            if (sender is Player) {
                val players: MutableList<Player?> = ArrayList()
                players.add(sender)
                return players
            }

            throw IllegalStateException()
        }

    companion object {
        private val SELECTOR_SYMBOL_MAP = HashMap<String, Selector>()

        init {
            SELECTOR_SYMBOL_MAP["@a"] = Selector.ALL
            SELECTOR_SYMBOL_MAP["@w"] = Selector.WORLD
            SELECTOR_SYMBOL_MAP["@wr"] = Selector.WORLD_RANDOM
            SELECTOR_SYMBOL_MAP["@r"] = Selector.RANDOM
            SELECTOR_SYMBOL_MAP["@s"] = Selector.SELF
        }

        val selectorStrings: Set<String>
            get() = SELECTOR_SYMBOL_MAP.keys
    }
}