package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Player

internal class ExecutorFragment : ExecutorAdapter("fragment") {
    init {
        addParameter(ParameterDisc(true))
    }

    /**
     * The function executes a player command in Kotlin, checking the version and adding a disc fragment to the player's
     * inventory.
     *
     * @param sender The `sender` parameter represents the player who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     * @return The function `executePlayer` returns a boolean value.
     */
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        if(VERSION < "1.19") {
            sender.sendJEXTMessage("command-not-supported", hashMapOf(
                "command" to "fragment",
                "reason" to "not supported in < 1.19"
            ))
        }

        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[0]
            ))

            return true
        }

        sender.inventory.addItem(DiscContainer(disc).fragmentItem)

        sender.sendJEXTMessage("disc-command-success", hashMapOf(
            "disc" to disc.TITLE
        ))

        return true
    }
}