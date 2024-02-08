package me.spartacus04.jext.webapi.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.config.fields.FieldGuiStyle
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.config.fields.FieldLanguageMode
import me.spartacus04.jext.webapi.utils.JextHttpHandler

class ConfigReadHandler : JextHttpHandler(true) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    override fun onGet(exchange: HttpExchange) {
        // ——————————————No GSON?——————————————
        //⠀⣞⢽⢪⢣⢣⢣⢫⡺⡵⣝⡮⣗⢷⢽⢽⢽⣮⡷⡽⣜⣜⢮⢺⣜⢷⢽⢝⡽⣝
        //⠸⡸⠜⠕⠕⠁⢁⢇⢏⢽⢺⣪⡳⡝⣎⣏⢯⢞⡿⣟⣷⣳⢯⡷⣽⢽⢯⣳⣫⠇
        //⠀⠀⢀⢀⢄⢬⢪⡪⡎⣆⡈⠚⠜⠕⠇⠗⠝⢕⢯⢫⣞⣯⣿⣻⡽⣏⢗⣗⠏⠀
        //⠀⠪⡪⡪⣪⢪⢺⢸⢢⢓⢆⢤⢀⠀⠀⠀⠀⠈⢊⢞⡾⣿⡯⣏⢮⠷⠁⠀⠀
        //⠀⠀⠀⠈⠊⠆⡃⠕⢕⢇⢇⢇⢇⢇⢏⢎⢎⢆⢄⠀⢑⣽⣿⢝⠲⠉⠀⠀⠀⠀
        //⠀⠀⠀⠀⠀⡿⠂⠠⠀⡇⢇⠕⢈⣀⠀⠁⠡⠣⡣⡫⣂⣿⠯⢪⠰⠂⠀⠀⠀⠀
        //⠀⠀⠀⠀⡦⡙⡂⢀⢤⢣⠣⡈⣾⡃⠠⠄⠀⡄⢱⣌⣶⢏⢊⠂⠀⠀⠀⠀⠀⠀
        //⠀⠀⠀⠀⢝⡲⣜⡮⡏⢎⢌⢂⠙⠢⠐⢀⢘⢵⣽⣿⡿⠁⠁⠀⠀⠀⠀⠀⠀⠀
        //⠀⠀⠀⠀⠨⣺⡺⡕⡕⡱⡑⡆⡕⡅⡕⡜⡼⢽⡻⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        //⠀⠀⠀⠀⣼⣳⣫⣾⣵⣗⡵⡱⡡⢣⢑⢕⢜⢕⡝⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        //⠀⠀⠀⣴⣿⣾⣿⣿⣿⡿⡽⡑⢌⠪⡢⡣⣣⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        //⠀⠀⠀⡟⡾⣿⢿⢿⢵⣽⣾⣼⣘⢸⢸⣞⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        //⠀⠀⠀⠀⠁⠇⠡⠩⡫⢿⣝⡻⡮⣒⢽⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        // —————————————Fuck Gson—————————————
        val response = """
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
        """.trimIndent()

        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.use { output ->
            output.write(response.toByteArray())
        }
    }
}