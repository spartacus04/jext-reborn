package me.spartacus04.jext.config

import me.spartacus04.jext.language.LanguageManager

class ConfigData {
    companion object {
        lateinit var CONFIG: Config
        lateinit var DISCS: List<Disc>
        lateinit var LANG: LanguageManager
    }
}