package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class ExecutorDiscGive : ExecutorAdapter("discgive") {
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
        val players = PlayerSelector(sender, args[0]).players ?: return true

        val disc = DISCS.find { it.DISC_NAMESPACE == args[0] }

        if (disc == null) {
            Log().eror().t("Disc with the namespace ").o(args[1]).t(" doesn't exists.").send(sender)
            return true
        }

        for (player in players) {
            player!!.inventory.addItem(DiscContainer(disc).discItem)
            Log().info().t("Received ").p().t(" disc.").send(player, disc)
        }

        val playerCount = players.size

        if (playerCount >= 2) {
            Log().warn().t("Given ").o().t(" disc to ").o().t(" players!").send(sender, disc, playerCount)
        } else if (playerCount == 1) {
            Log().okay().t("Given ").o().t(" disc to ").o(players[0]!!.displayName).t(".").send(sender, disc)
        } else {
            Log().eror().t("Given disc to no player, something went wrong!").send(sender)
        }
        return true
    }
}