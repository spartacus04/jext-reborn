package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.entity.Player

/**
 * ExecutorDisc is a class used to register the "disc" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorDisc : ExecutorAdapter("disc") {
    init {
        addParameter(ParameterDisc(true))
    }

    /**
     * The function `executePlayer` gives the sender a disc.
     *
     * @param sender The player who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun executePlayer(sender: Player, args: Array<String>) {
        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendJEXTMessage("disc-namespace-not-found", hashMapOf(
                "namespace" to args[0]
            ))

            return
        }

        sender.inventory.addItem(disc.discItemStack)

        sender.sendJEXTMessage("disc-command-success", hashMapOf(
            "disc" to disc.displayName
        ))

        return
    }
}