package me.spartacus04.jext.config.fields

import com.google.gson.annotations.SerializedName

enum class FieldJukeboxBehaviour {
    @SerializedName("vanilla")
    VANILLA,

    @SerializedName("gui-scroll-vertical", alternate = ["gui"])
    SCROLL_VERTICAL,

    @SerializedName("gui-scroll-horizontal")
    SCROLL_HORIZONTAL,

    @SerializedName("gui-page-horizontal")
    PAGE_HORIZONTAL,

    @SerializedName("gui-tab-vertical")
    TAB_VERTICAL,

    @SerializedName("gui-tab-horizontal")
    TAB_HORIZONTAL;

    companion object {
        fun fromString(name: String): FieldJukeboxBehaviour {
            return entries.find { it.name == name || it.name == name.replace("-", "_").uppercase() } ?: throw IllegalArgumentException("Invalid serialized name")
        }
    }
}