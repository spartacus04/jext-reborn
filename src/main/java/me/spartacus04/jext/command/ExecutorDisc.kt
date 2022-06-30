package me.spartacus04.jext.command

import me.spartacus04.jext.Log
import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.disc.DiscContainer
import org.bukkit.entity.Player

internal class ExecutorDisc : ExecutorAdapter("disc") {
    init {
        addParameter(ParameterDisc(true))
    }

    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        val disc = DISCS.find { it.DISC_NAMESPACE == args[0] }

        if (disc == null) {
            Log().eror().t("Disc with the namespace ").o(args[0]).t(" doesn't exists.").send(sender)
            return true
        }

        sender.inventory.addItem(DiscContainer(disc).discItem)

        Log().info().t("Obtained ").p().t(" disc.").send(sender, disc)
        return true
    }
}