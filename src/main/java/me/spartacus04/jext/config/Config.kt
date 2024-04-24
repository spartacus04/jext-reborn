package me.spartacus04.jext.config

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.fields.FieldGuiStyle
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.config.fields.FieldLanguageMode
import me.spartacus04.jext.utils.FileBind

data class Config(
    // behaviour settings

    /*
    [
            {
                "name": "Language mode",
                "id": "lang",
                "description": "If set to auto, the plugin will use the player's locale; if set to silent, the plugin won't output any messages; if set to custom the plugin will use the language specified in the custom language file; If set to a locale, the plugin will use that locale.",
                "value": "${CONFIG.LANGUAGE_MODE.toLocale()}",
                "defaultValue": "auto",
                "enumValues": [${FieldLanguageMode.entries.joinToString(", ") { "\"${it.toLocale()}\"" }}]
            },
            {
                "name": "Jukebox behaviour",
                "id": "jukebox-behaviour",
                "description": "If set to vanilla, the plugin will use the vanilla jukebox behaviour; if set to gui, the plugin will use the custom GUI.",
                "value": "${CONFIG.JUKEBOX_BEHAVIOUR.toJukeboxBehaviour()}",
                "defaultValue": "vanilla",
                "enumValues": [${FieldJukeboxBehaviour.entries.joinToString(", ") { "\"${it.toJukeboxBehaviour()}\"" }}]
            },
            {
                "name": "Jukebox GUI style",
                "id": "jukebox-gui-style",
                "description": "Determines the style of the jukebox GUI.",
                "value": "${CONFIG.GUI_STYLE.toGuiStyle()}",
                "defaultValue": "scroll-vertical",
                "enumValues": [${FieldGuiStyle.entries.joinToString(", ") { "\"${it.toGuiStyle()}\"" }}]
            },
            {
                "name": "Jukebox GUI size",
                "id": "jukebox-gui-size",
                "description": "Sets the maximum amount of items that can be added to a jukebox GUI.",
                "value": ${CONFIG.GUI_SIZE},
                "defaultValue": 96
            },
            {
                "name": "Disable music overlap",
                "id": "disable-music-overlap",
                "description": "If set to true, the plugin will not play music if there is already music playing.",
                "value": ${CONFIG.DISABLE_MUSIC_OVERLAP},
                "defaultValue": true
            },
            {
                "name": "Disc loot tables limit",
                "id": "disc-loottables-limit",
                "description": "Sets the maximum amount of discs that can be found in chests, the default amount is 2.",
                "value": ${gson.toJson(CONFIG.DISC_LIMIT)},
                "defaultValue": {},
                "enumValues": "chests/*"
            },
            {
                "name": "Disc fragments loot tables limit",
                "id": "fragment-loottables-limit",
                "description": "Sets the maximum amount of disc fragments that can be found in chests, the default amount is 3.",
                "value": ${gson.toJson(CONFIG.FRAGMENT_LIMIT)},
                "defaultValue": {},
                "enumValues": "chests/*"
            },
            {
                "name": "Check for updates",
                "id": "check-for-updates",
                "description": "If set to true, the plugin will check for updates on startup.",
                "value": ${CONFIG.CHECK_FOR_UPDATES},
                "defaultValue": true
            },
            {
                "name": "Allow metrics",
                "id": "allow-metrics",
                "description": "If set to true, the plugin will send metrics to bStats. Please consider enabling this, as it helps me improve the plugin.",
                "value": ${CONFIG.ALLOW_METRICS},
                "defaultValue": true
            },
            {
                "name": "Force resource pack",
                "id": "force-resource-pack",
                "description": "If set to true, the plugin will force players to use the resource pack. If the player declines, they will be kicked.",
                "value": ${CONFIG.FORCE_RESOURCE_PACK},
                "defaultValue": false
            },
            {
                "name": "Enable resource pack host",
                "id": "enable-resource-pack-host",
                "description": "If set to true, the plugin will host the resource pack and automatically send it to players.",
                "value": ${CONFIG.RESOURCE_PACK_HOST},
                "defaultValue": true
            },
            {
                "name": "Web interface port",
                "id": "web-interface-port",
                "description": "The port the web interface api & resource pack will be hosted on.",
                "value": ${CONFIG.WEB_INTERFACE_PORT},
                "defaultValue": 9871
            },
            {
                "name": "Web interface api enabled",
                "id": "web-interface-api-enabled",
                "description": "If set to true, the plugin will have a REST api running to edit the discs settings and the config.",
                "value": ${CONFIG.WEB_INTERFACE_API_ENABLED},
                "defaultValue": true
            },
            {
                "name": "Web interface password",
                "id": "web-interface-password",
                "description": "The password required to access the web interface api.",
                "value": "${CONFIG.WEB_INTERFACE_PASSWORD}",
                "defaultValue": ""
            }
        ]
        """

     */ */ */

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
) : FileBind("config.json", object : TypeToken<Config>() {})
