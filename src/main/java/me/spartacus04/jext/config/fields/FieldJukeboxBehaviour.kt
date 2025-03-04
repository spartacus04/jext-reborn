package me.spartacus04.jext.config.fields

import com.google.gson.annotations.SerializedName

enum class FieldJukeboxBehaviour {
    @SerializedName("vanilla")
    VANILLA,

    @SerializedName("gui")
    GUI;


    internal companion object {
        fun fromString(name: String): FieldJukeboxBehaviour {
            return entries.find { it.name == name || it.name == name.replace("-", "_").uppercase() } ?: throw IllegalArgumentException("Invalid serialized name")
        }
    }
}