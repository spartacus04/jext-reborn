package me.spartacus04.jext.commands

import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterDisc
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.entity.Player

/**
 * ExecutorFragment is a class used to register the "fragment" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorFragment : ExecutorAdapter("fragment") {
    init {
        addParameter(ParameterDisc(true))
    }

    /**
     * The function `executePlayer` gives the sender a disc fragment.
     *
     * @param sender The player who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun executePlayer(sender: Player, args: Array<String>) {
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

            return
        }

        sender.inventory.addItem(disc.fragmentItemStack)

        sender.sendJEXTMessage("disc-command-success", hashMapOf(
            "disc" to disc.displayName
        ))

        return
    }
}