package me.spartacus04.jext.config.fields

import com.google.gson.annotations.SerializedName

enum class FieldGuiStyle {
    @SerializedName("gui-scroll-vertical")
    SCROLL_VERTICAL,

    @SerializedName("gui-scroll-horizontal")
    SCROLL_HORIZONTAL,

    @SerializedName("gui-page-horizontal")
    PAGE_HORIZONTAL,

    @SerializedName("gui-page-vertical")
    PAGE_VERTICAL;

    companion object {
        fun fromString(name: String): FieldGuiStyle {
            return FieldGuiStyle.entries.find { it.name == name || it.name == name.replace("-", "_").uppercase() } ?: throw IllegalArgumentException("Invalid serialized name")
        }
    }
}