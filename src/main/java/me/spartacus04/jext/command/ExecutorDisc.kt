package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Player

internal class ExecutorDisc : ExecutorAdapter("disc") {
    init {
        addParameter(ParameterDisc(true))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val disc = ParameterDisc.getDisc(args[0])

        if (disc == null) {
            sender.sendMessage(
                "[§aJEXT§f]  ${LANG.DISC_NAMESPACE_NOT_FOUND}"
                    .replace("%namespace%", args[0])
            )
            return true
        }

        sender.inventory.addItem(DiscContainer(disc).discItem)

        sender.sendMessage(
            "[§aJEXT§f]  ${LANG.DISC_COMMAND_SUCCESS}"
                .replace("%disc%", disc.TITLE)
        )
        return true
    }
}