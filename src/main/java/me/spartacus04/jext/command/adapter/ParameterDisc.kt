package me.spartacus04.jext.command.adapter

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import me.spartacus04.jext.config.Disc
import org.bukkit.command.CommandSender

internal class ParameterDisc(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "namespace"

    /**
     * The function filters a list of strings based on a given parameter and returns the matching strings, or null if there
     * are no matches.
     *
     * @param parameter A string parameter that represents the input value to be matched against the list of discs.
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     * @return The method is returning a list of strings.
     */
    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        val discs = DISCS.map { it.DISC_NAMESPACE }

        val matches = discs.filter { it.startsWith(parameter, true) }

        return matches.ifEmpty { return null }
    }

    companion object {
        /**
         * The function "getDisc" returns a Disc object based on a given parameter.
         *
         * @param parameter The parameter is a string that represents the namespace of a disc.
         * @return a Disc object or null.
         */
        fun getDisc(parameter: String) : Disc? {
            return DISCS.find { it.DISC_NAMESPACE == parameter }
        }
    }
}