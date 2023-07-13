package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.command.adapter.ParameterNumber
import me.spartacus04.jext.command.adapter.ParameterPlayer
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorPlayMusic : ExecutorAdapter("playmusic") {
    init {
        addParameter(ParameterPlayer(true))
        addParameter(ParameterDisc(true))
        addParameter(ParameterNumber(false, 0.5f, 1.0f, 1.5f).setName("pitch"))
        addParameter(ParameterNumber(false, 4.0f, 1.0f, 0.5f).setName("volume"))
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
     * The function `mergedExecute` plays a music disc or sound effect to one or multiple players in a Minecraft server,
     * with options for pitch and volume.
     *
     * @param sender The `sender` parameter is of type `CommandSender`, which represents the entity that executed the
     * command. It could be a player, console, or command block.
     * @param args args is an array of strings that contains the command arguments. The first element (args[0]) is expected
     * to be the player name or selector, the second element (args[1]) is expected to be the disc namespace, the third
     * element (args[2]) is optional and represents the pitch
     * @return The function does not have a return type, so it does not explicitly return anything. However, it does have
     * several `return` statements within the function that can be used to exit the function early and return control to
     * the caller.
     */
    private fun mergedExecute(sender: CommandSender, args: Array<String>) {
        val players = ParameterPlayer.getPlayers(args[0], sender)

        val disc = ParameterDisc.getDisc(args[1])
        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[1]
            ))

            return
        }

        val discContainer = DiscContainer(disc)

        var pitch = 1.0f
        if (args.size >= 3) {
            pitch = try {
                args[2].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "pitch"
                ))

                return
            }
        }

        var isMusic = true

        var volume = 4.0f
        if (args.size >= 4) {
            isMusic = false

            try {
                volume = args[3].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendJEXTMessage("wrong-number-format", hashMapOf(
                    "param" to "volume"
                ))

                return
            }
        }

        for (player in players) {
            if (isMusic) {
                player.playSound(player.location, DiscContainer(disc).namespace, SoundCategory.RECORDS, Float.MAX_VALUE, pitch)
                sender.sendJEXTMessage("music-now-playing", hashMapOf(
                    "name" to disc.TITLE
                ))
            } else {
                discContainer.play(player, pitch, volume)
            }
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            sender.sendJEXTMessage("played-music-to-multiple", hashMapOf(
                "name" to disc.TITLE,
                "playercount" to playerCount.toString()
            ))
        } else if (playerCount == 1) {
            sender.sendJEXTMessage("played-music-to", hashMapOf(
                "name" to disc.TITLE,
                "player" to players[0].name
            ))
        } else {
            sender.sendJEXTMessage("played-music-to-no-one")
        }
    }
}