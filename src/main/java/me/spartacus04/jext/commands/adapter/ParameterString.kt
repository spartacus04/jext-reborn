package me.spartacus04.jext.commands.adapter

import org.bukkit.command.CommandSender

internal class ParameterString internal constructor(required: Boolean, private val suggestedValues: List<String>) : Parameter(required) {
    override val name: String = "string"

    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        return if (parameter.isEmpty()) {
            suggestedValues
        } else null
    }
}