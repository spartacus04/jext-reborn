package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.send
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Player

internal class ExecutorDisc : ExecutorAdapter("disc") {
    init {
        addParameter(ParameterDisc(true))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.send(
                LANG.format(sender, "disc-namespace-not-found")
                    .replace("%namespace%", args[0])
            )
            return true
        }

        sender.inventory.addItem(DiscContainer(disc).discItem)

        sender.send(
            LANG.format(sender, "disc-command-success")
                .replace("%disc%", disc.TITLE)
        )
        return true
    }
}