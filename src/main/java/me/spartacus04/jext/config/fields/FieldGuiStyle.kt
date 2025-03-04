package me.spartacus04.jext.config.fields

import com.google.gson.annotations.SerializedName

enum class FieldGuiStyle {
    @SerializedName("scroll-vertical")
    SCROLL_VERTICAL,

    @SerializedName("scroll-horizontal")
    SCROLL_HORIZONTAL,

    @SerializedName("page-horizontal")
    PAGE_HORIZONTAL,

    @SerializedName("page-vertical")
    PAGE_VERTICAL;

    @Suppress("unused")
    internal companion object {
        fun fromString(name: String): FieldGuiStyle {
            return FieldGuiStyle.entries.find { it.name == name || it.name == name.replace("-", "_").uppercase() } ?: throw IllegalArgumentException("Invalid serialized name")
        }
    }
}