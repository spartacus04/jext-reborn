package me.spartacus04.jext.commands.adapter

import org.bukkit.command.CommandSender

/**
 * This class represents a custom number parameter
 *
 * @constructor Creates a new ParameterNumber object
 *
 * @param required A boolean value that represents whether the parameter is required or not
 * @param suggestedValues A list of suggested values
 */
class ParameterNumber internal constructor(required: Boolean, vararg suggestedValues: Number) : Parameter(required) {
    private val stringValues: MutableList<String>
    override var name: String? = null
        get() {
            return if (field == null) field else "number"
        }

    init {
        stringValues = ArrayList()
        for (value in suggestedValues) {
            stringValues.add(value.toString())
        }
    }

    /**
     * This function sets the name of the parameter
     *
     * @param name The name of the parameter
     * @return The current ParameterNumber object
     */
    fun setName(name: String?): ParameterNumber {
        this.name = name
        return this
    }

    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        return if (parameter.isEmpty()) {
            stringValues
        } else null
    }
}