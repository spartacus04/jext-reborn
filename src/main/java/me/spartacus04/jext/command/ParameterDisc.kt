package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import org.bukkit.command.CommandSender
import java.util.*

internal class ParameterDisc(required: Boolean) : Parameter(required) {
    override val name: String
        get() = "namespace"

    override fun onComplete(parameter: String, sender: CommandSender?): List<String>? {
        val namespaces = DISCS.map { it.DISC_NAMESPACE }

        val matches: MutableList<String> = ArrayList()

        for (namespace in namespaces) {
            if (matches.size >= 8) break

            if (namespace.lowercase(Locale.getDefault()).startsWith(parameter.lowercase(Locale.getDefault()))) {
                matches.add(namespace)
            }
        }

        return if (matches.size > 0) matches.toList() else return null
    }
}