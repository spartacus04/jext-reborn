package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.command.adapter.ParameterDisc
import me.spartacus04.jext.config.sendJEXTMessage
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Player

internal class ExecutorDisc : ExecutorAdapter("disc") {
    init {
        addParameter(ParameterDisc(true))
    }

    /**
     * The function executes a player command in Kotlin, adding a disc item to the player's inventory and sending success
     * messages.
     *
     * @param sender The `sender` parameter is of type `Player`, which represents the player who executed the command.
     * @param args An array of strings representing the arguments passed to the command.
     * @return a boolean value.
     */
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[0]
            ))

            return true
        }

        sender.inventory.addItem(DiscContainer(disc).discItem)

        sender.sendJEXTMessage("disc-command-success", hashMapOf(
            "disc" to disc.TITLE
        ))

        return true
    }
}