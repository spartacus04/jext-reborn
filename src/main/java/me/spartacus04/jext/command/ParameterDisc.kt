package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.Disc
import org.bukkit.command.CommandSender

internal class ParameterDisc(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "namespace"

    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        val discs = DISCS.map { it.DISC_NAMESPACE }

        val matches = discs.filter { it.startsWith(parameter, true) }

        return matches.ifEmpty { return null }
    }

    companion object {
        fun getDisc(parameter: String) : Disc? {
            return DISCS.find { it.DISC_NAMESPACE == parameter }
        }
    }
}