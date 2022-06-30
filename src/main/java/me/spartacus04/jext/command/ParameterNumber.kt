package me.spartacus04.jext.command

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

    fun setName(name: String?): ParameterNumber {
        this.name = name
        return this
    }

    override fun onComplete(parameter: String, sender: CommandSender?): List<String>? {
        return if (parameter.isEmpty()) {
            stringValues
        } else null
    }
}