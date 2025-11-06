package me.spartacus04.jext.config.legacy

import com.google.gson.annotations.SerializedName
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.jext.config.Config
import me.spartacus04.jext.config.fields.FieldGuiStyle
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.config.fields.FieldLanguageMode

/**
 * The data class `Config` is used to store the plugin's configuration settings.
 */
@Suppress("PropertyName")
internal data class V8Config(
    @SerializedName("lang")
    var LANGUAGE_MODE: FieldLanguageMode = FieldLanguageMode.AUTO,

    @SerializedName("jukebox-behaviour")
    var JUKEBOX_BEHAVIOUR : FieldJukeboxBehaviour = FieldJukeboxBehaviour.VANILLA,

    @SerializedName("jukebox-gui-style")
    var GUI_STYLE : FieldGuiStyle = FieldGuiStyle.SCROLL_VERTICAL,

    @SerializedName("jukebox-gui-size")
    var GUI_SIZE : Int = 96,

    @SerializedName("disable-music-overlap")
    var DISABLE_MUSIC_OVERLAP : Boolean = true,

    @SerializedName("jukebox-range")
    var JUKEBOX_RANGE: Int = 64,

    @SerializedName("disc-loottables-limit")
    var DISC_LIMIT : HashMap<String, Int> = HashMap(),

    @SerializedName("fragment-loottables-limit")
    var FRAGMENT_LIMIT : HashMap<String, Int> = HashMap(),

    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean = false,

    @SerializedName("check-for-updates")
    var CHECK_FOR_UPDATES : Boolean = true,

    @SerializedName("allow-metrics")
    var ALLOW_METRICS : Boolean = true,

    @SerializedName("enable-resource-pack-host")
    var RESOURCE_PACK_HOST : Boolean = true,

    @SerializedName("web-interface-port")
    var WEB_INTERFACE_PORT : Int = 9871,

    @SerializedName("override-web-interface-base-url")
    var WEB_INTERFACE_BASE_URL: String = "",

    @SerializedName("web-interface-api-enabled")
    var WEB_INTERFACE_API_ENABLED : Boolean = true,

    @SerializedName("web-interface-password")
    var WEB_INTERFACE_PASSWORD : String = "",
) : ConfigMigrator {
    override fun migrateToNext(plugin: ColosseumPlugin): String {
        return plugin.gson.toJson(Config(
            LANGUAGE_MODE = LANGUAGE_MODE,
            JUKEBOX_BEHAVIOUR = JUKEBOX_BEHAVIOUR,
            GUI_STYLE = GUI_STYLE,
            GUI_SIZE = GUI_SIZE,
            DISABLE_MUSIC_OVERLAP = DISABLE_MUSIC_OVERLAP,
            DISC_LIMIT = DISC_LIMIT,
            FRAGMENT_LIMIT = FRAGMENT_LIMIT,
            FORCE_RESOURCE_PACK = FORCE_RESOURCE_PACK,
            CHECK_FOR_UPDATES = CHECK_FOR_UPDATES,
            ALLOW_METRICS = ALLOW_METRICS,
            RESOURCE_PACK_HOST = RESOURCE_PACK_HOST,
            WEB_INTERFACE_BASE_URL = WEB_INTERFACE_BASE_URL,
            WEB_INTERFACE_PORT = WEB_INTERFACE_PORT,
            WEB_INTERFACE_API_ENABLED = WEB_INTERFACE_API_ENABLED,
            WEB_INTERFACE_PASSWORD = WEB_INTERFACE_PASSWORD
        ))
    }
}