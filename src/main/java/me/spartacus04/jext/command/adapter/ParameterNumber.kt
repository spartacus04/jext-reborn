package me.spartacus04.jext.command.adapter

import org.bukkit.command.CommandSender

class ParameterNumber internal constructor(required: Boolean, vararg suggestedValues: Float) : Parameter(required) {
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
     * The function sets the name parameter and returns the object itself.
     *
     * @param name A nullable String parameter named "name".
     * @return The method `setName` is returning an instance of the `ParameterNumber` class.
     */
    fun setName(name: String?): ParameterNumber {
        this.name = name
        return this
    }

    /**
     * The function returns a list of string values if the parameter is empty, otherwise it returns null.
     *
     * @param parameter A string parameter that represents the input value for the command.
     * @param sender The `sender` parameter is of type `CommandSender`. It represents the entity that executed the command.
     * It could be a player, console, or any other entity that has the ability to execute commands.
     * @return The method is returning a List<String> or null.
     */
    override fun onComplete(parameter: String, sender: CommandSender): List<String>? {
        return if (parameter.isEmpty()) {
            stringValues
        } else null
    }
}