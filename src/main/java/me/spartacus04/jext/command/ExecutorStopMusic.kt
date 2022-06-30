package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
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

    fun mergedExecute(sender: CommandSender, args: Array<String>): Boolean {
        val selector = PlayerSelector(sender, args[0])
        val players = selector.players ?: return true
        val namespaces: MutableSet<String>

        if (args.size <= 1) {
            namespaces = DISCS.map { it.DISC_NAMESPACE }.toMutableSet()
        } else {
            val disc = DISCS.find { it.DISC_NAMESPACE == args[0] }

            if (disc == null) {
                Log().eror().t("Music with the namespace ").o(args[1]).t(" doesn't exists.").send(sender)
                return true
            }

            namespaces = HashSet()

            namespaces.add(DiscContainer(disc).namespace)
        }

        for (player in players) {

            for (namespace in namespaces) {

                player!!.stopSound(namespace, SoundCategory.RECORDS)
                if (namespaces.size == 1) Log().info().t("Stopped music ").p().t(".")
                    .send(player, DISCS.find { it.DISC_NAMESPACE == namespace })
            }

            if (namespaces.size > 1) Log().info().t("Stopped all music.").send(player)
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            Log().warn().t("Stopped music for ").o().t(" players!").send(sender, playerCount)
        } else if (playerCount == 1) {
            Log().okay().t("Stopped music for ").o(players[0]!!.name).t(".").send(sender)
        } else {
            Log().eror().t("Stopped music to no player, something might when wrong!").send(sender)
        }

        return true
    }
}