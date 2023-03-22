package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData
import me.spartacus04.jext.config.send
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Player

internal class ExecutorFragment : ExecutorAdapter("fragment") {
    init {
        addParameter(ParameterDisc(true))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            ConfigData.LANG.format(sender, "disc-namespace-not-found")
                .replace("%namespace%", args[0])
                .let { sender.send(it) }

            return true
        }

        sender.inventory.addItem(DiscContainer(disc).fragmentItem)

        ConfigData.LANG.format(sender, "disc-command-success")
            .replace("%disc%", disc.TITLE)
            .let { sender.send(it) }

        return true
    }
}