package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorStopMusic : ExecutorAdapter("stopmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        return mergedExecute(sender, args)
    }

    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        return mergedExecute(sender, args)
    }

    private fun mergedExecute(sender: CommandSender, args: Array<String>): Boolean {
        val selector = PlayerSelector(sender, args[0])
        val players = selector.players ?: return true
        val namespaces: MutableSet<String>

        if (args.size <= 1) {
            namespaces = DISCS.map { it.DISC_NAMESPACE }.toMutableSet()
        } else {
            val disc = DISCS.find { it.DISC_NAMESPACE == args[0] }

            if (disc == null) {
                sender.sendMessage(
                    "[§aJEXT§f]  ${LANG.DISC_NAMESPACE_NOT_FOUND}"
                        .replace("%namespace%", args[0])
                )
                return true
            }

            namespaces = HashSet()

            namespaces.add(DiscContainer(disc).namespace)
        }

        for (player in players) {

            for (namespace in namespaces) {

                player!!.stopSound(namespace, SoundCategory.RECORDS)
                if (namespaces.size == 1) {
                    player.sendMessage(
                        "[§aJEXT§f]  ${LANG.STOPPED_MUSIC}"
                            .replace("%name%", DISCS.find { it.DISC_NAMESPACE == namespace }!!.TITLE)
                    )
                }
            }

            if (namespaces.size > 1) {
                player!!.sendMessage(
                    "[§aJEXT§f]  ${LANG.STOPPED_ALL_MUSIC}"
                )
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.STOPPED_MUSIC_FOR_MULTIPLE}"
                    .replace("%playercount%", playerCount.toString())
            )
        } else if (playerCount == 1) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.STOPPED_MUSIC_FOR}"
                    .replace("%playername%", players[0]!!.name)
            )
        } else {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.STOPPED_MUSIC_FOR_NO_ONE}"
            )
        }

        return true
    }
}