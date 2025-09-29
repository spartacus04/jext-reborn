package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.config.fields.FieldGuiStyle
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.config.fields.FieldLanguageMode
import me.spartacus04.jext.utils.FileBind

/**
 * The data class `Config` is used to store the plugin's configuration settings.
 */
@Suppress("PropertyName")
data class Config(
    // behaviour settings

    @ConfigField(
        "Language mode",
        "If set to auto, the plugin will use the player's locale; if set to silent, the plugin won't output any messages; if set to custom the plugin will use the language specified in the custom language file; If set to a locale, the plugin will use that locale.",
        "\"auto\"",
        "[\"auto\", \"custom\", \"silent\", \"af_za\", \"ar_sa\", \"ca_es\", \"cs_cz\", \"da_dk\", \"de_de\", \"el_gr\", \"en_us\", \"es_es\", \"fi_fi\", \"fr_fr\", \"he_il\", \"hu_hu\", \"it_it\", \"ja_jp\", \"ko_kr\", \"nl_nl\", \"no_no\", \"pl_pl\", \"pt_br\", \"pt_pt\", \"ro_ro\", \"ru_ru\", \"sr_sp\", \"sv_se\", \"tr_tr\", \"uk_ua\", \"vi_vn\", \"zh_cn\", \"zh_tw\"]"
    )
    @SerializedName("lang")
    var LANGUAGE_MODE: FieldLanguageMode = FieldLanguageMode.AUTO,

    @ConfigField(
        "Jukebox behaviour",
        "If set to vanilla, the plugin will use the vanilla jukebox behaviour; if set to gui, the plugin will use the custom GUI.",
        "\"vanilla\"",
        "[\"vanilla\", \"gui\"]"
    )
    @SerializedName("jukebox-behaviour")
    var JUKEBOX_BEHAVIOUR : FieldJukeboxBehaviour = FieldJukeboxBehaviour.VANILLA,

    @ConfigField(
        "Jukebox GUI style",
        "Determines the style of the jukebox GUI.",
        "\"scroll-vertical\"",
        "[\"scroll-vertical\", \"scroll-horizontal\", \"page-vertical\", \"page-horizontal\"]"
    )
    @SerializedName("jukebox-gui-style")
    var GUI_STYLE : FieldGuiStyle = FieldGuiStyle.SCROLL_VERTICAL,

    @ConfigField(
        "Jukebox GUI size",
        "Sets the maximum amount of items that can be added to a jukebox GUI.",
        "96"
    )
    @SerializedName("jukebox-gui-size")
    var GUI_SIZE : Int = 96,

    @ConfigField(
        "Disable music overlap",
        "If set to true, the plugin will not play music if there is already music playing.",
        "true"
    )
    @SerializedName("disable-music-overlap")
    var DISABLE_MUSIC_OVERLAP : Boolean = true,

    @ConfigField(
        "Jukebox range",
        "Sets the range for sound playback, if set to values <= 0 it'll play for everyone on the server. Only works if the audio is mono",
        "64"
    )
    @SerializedName("jukebox-range")
    var JUKEBOX_RANGE: Int = 64,

    @ConfigField(
        "Disc loot tables limit",
        "Sets the maximum amount of discs that can be found in chests, the default amount is 2.",
        "{}",
        "\"chests/*\""
    )
    @SerializedName("disc-loottables-limit")
    var DISC_LIMIT : HashMap<String, Int> = HashMap(),

    @ConfigField(
        "Disc fragments loot tables limit",
        "Sets the maximum amount of disc fragments that can be found in chests, the default amount is 3.",
        "{}",
        "\"chests/*\""
    )
    @SerializedName("fragment-loottables-limit")
    var FRAGMENT_LIMIT : HashMap<String, Int> = HashMap(),

    @ConfigField(
        "Force resource pack",
        "If set to true, the plugin will force players to use the resource pack. If the player declines, they will be kicked.",
        "false"
    )
    @SerializedName("force-resource-pack")
    var FORCE_RESOURCE_PACK : Boolean = false,

    @ConfigField(
        "Resource pack URL",
        "If set, the plugin use this URL and the resource-pack-hash for the resource pack source.",
        ""
    )
    @SerializedName("resource-pack-url")
    var RESOURCE_PACK_URL : String = "",

    @ConfigField(
        "Resource pack hash",
        "Hash for the pack in resource-pack-url.",
        ""
    )
    @SerializedName("resource-pack-hash")
    var RESOURCE_PACK_HASH : String = "",

    // metrics & updates

    @ConfigField(
        "Check for updates",
        "If set to true, the plugin will check for updates on startup.",
        "true"
    )
    @SerializedName("check-for-updates")
    var CHECK_FOR_UPDATES : Boolean = true,

    @ConfigField(
        "Allow metrics",
        "If set to true, the plugin will send metrics to bStats. Please consider enabling this, as it helps me improve the plugin.",
        "true"
    )
    @SerializedName("allow-metrics")
    var ALLOW_METRICS : Boolean = true,

    // web-api

    @ConfigField(
        "Enable resource pack host",
        "If set to true, the plugin will host the resource pack and automatically send it to players.",
        "true"
    )
    @SerializedName("enable-resource-pack-host")
    var RESOURCE_PACK_HOST : Boolean = true,

    @ConfigField(
        "Web interface port",
        "The port the web interface api & resource pack will be hosted on.",
        "9871"
    )
    @SerializedName("web-interface-port")
    var WEB_INTERFACE_PORT : Int = 9871,

    @ConfigField(
        "Web interface base url",
        "If set to something it'll use the custom base url for both the web interface api & resource pack host",
        "\"\""
    )
    @SerializedName("override-web-interface-base-url")
    var WEB_INTERFACE_BASE_URL: String = "",

    @ConfigField(
        "Web interface api enabled",
        "If set to true, the plugin will have a REST api running to edit the discs settings and the config.",
        "true"
    )
    @SerializedName("web-interface-api-enabled")
    var WEB_INTERFACE_API_ENABLED : Boolean = true,

    @ConfigField(
        "Web interface password",
        "The password required to access the web interface api.",
        "\"\""
    )
    @SerializedName("web-interface-password")
    var WEB_INTERFACE_PASSWORD : String = "",
) : FileBind("config.json", Config::class.java) {
    @Suppress("unused")
    @SerializedName("\$schema")
    private val schema = "https://raw.githubusercontent.com/spartacus04/jext-reborn/master/schemas/config.json"
}
