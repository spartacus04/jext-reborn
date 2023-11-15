package me.spartacus04.jext.commands.adapter

import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.discs.Disc
import org.bukkit.command.CommandSender

/**
 * This class represents a custom music disc parameter
 *
 * @constructor Creates a new ParameterDisc object
 *
 * @param required A boolean value that represents whether the parameter is required or not
 */
class ParameterDisc(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "namespace"

    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        val discs = DISCS.map { it.namespace }

        val matches = discs.filter { it.startsWith(parameter, true) }

        return matches.ifEmpty { return null }
    }

    companion object {

        /**
         * The function returns a Disc object based on a given parameter
         *
         * @param parameter The disc namespace
         * @return A Disc object or null
         */
        fun getDisc(parameter: String) : Disc? {
            return DISCS[parameter]
        }
    }
}