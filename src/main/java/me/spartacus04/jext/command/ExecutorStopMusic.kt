package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorStopMusic : ExecutorAdapter("stopmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(false))
    }

    /**
     * The function executes a command for a player in Kotlin.
     *
     * @param sender The "sender" parameter is of type "Player", which represents the player who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     * @return The method is returning a boolean value of true.
     */
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        mergedExecute(sender, args)
        return true
    }

    /**
     * The function executes a command and returns a boolean value indicating success.
     *
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value of true.
     */
    override fun executeCommand(sender: CommandSender, args: Array<String>): Boolean {
        mergedExecute(sender, args)
        return true
    }

    /**
     * The function `mergedExecute` stops music for specified players and namespaces, and sends messages to the sender
     * based on the number of players affected.
     *
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or command block.
     * @param args An array of strings representing the command arguments.
     * @return Nothing is being returned. The function is of type `Unit`, which means it does not return any value.
     */
    private fun mergedExecute(sender: CommandSender, args: Array<String>) {
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val namespaces: MutableSet<String>

        if (args.size <= 1) {
            namespaces = DISCS.map { it.DISC_NAMESPACE }.toMutableSet()
        } else {
            val disc = ParameterDisc.getDisc(args[1])

            if (disc == null) {
                sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                    "namespace" to args[1]
                ))

                return
            }

            namespaces = HashSet()

            namespaces.add(DiscContainer(disc).namespace)
        }

        for (player in players) {

            for (namespace in namespaces) {

                player.stopSound(namespace, SoundCategory.RECORDS)
                if (namespaces.size == 1) {
                    sender.sendJEXTMessage("stopped-music", hashMapOf(
                        "name" to DISCS.find { it.DISC_NAMESPACE == namespace }!!.TITLE
                    ))
                }
            }

            if (namespaces.size > 1) {
                sender.sendJEXTMessage("stopped-all-music")
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("stopped-music-for-multiple", hashMapOf(
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("stopped-music-for", hashMapOf(
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("stopped-music-for-no-one")
        }
    }
}