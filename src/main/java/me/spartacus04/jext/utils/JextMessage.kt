package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.config.fields.FieldLanguageMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * The function gets a value from the language map, replaces the parameters, and sends it to the command sender.
 *
 * @param key The language key to get the value from.
 * @param params A hashmap containing the parameters to replace.
 */
internal fun CommandSender.sendJEXTMessage(key: String, params: HashMap<String, String> = HashMap()) {
    if(this !is Player) {
        return sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG["en_us", key]}", params)
        )
    }

    when(JextState.CONFIG.LANGUAGE_MODE) {
        FieldLanguageMode.AUTO -> sendMessage(
            if(LANG.hasLanguage(this.locale.lowercase())) {
                LANG.replaceParameters("[§aJEXT§f] ${LANG[this.locale.lowercase(), key]}", params)
            } else {
                LANG.replaceParameters("[§aJEXT§f] ${LANG["en_us", key]}", params)
            }
        )
        FieldLanguageMode.CUSTOM -> sendMessage(
            LANG.replaceParameters("[§aJEXT§f] ${LANG["custom", key]}", params)
        )
        FieldLanguageMode.SILENT -> {}
        else -> {
            val lang = JextState.CONFIG.LANGUAGE_MODE.name.lowercase()

            sendMessage(
                LANG.replaceParameters("[§aJEXT§f] ${LANG[lang, key]}", params)
            )
        }
    }
}