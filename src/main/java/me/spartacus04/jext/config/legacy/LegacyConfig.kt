package me.spartacus04.jext.config.legacy

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.spartacus04.jext.JextState.PLUGIN

internal open class LegacyConfig <T>(
    private val addList: List<String>? = null,
    private val removeList: List<String>? = null,
    private val replaceTokens: (defaultConfig: String, legacyConfig: T) -> String
) {
    private fun canUpdate(legacyConfigText: String) : Boolean {
        val addListOK = addList?.all { !legacyConfigText.contains(it) } ?: false
        val removeListOK = removeList?.all { legacyConfigText.contains(it) } ?: false

        return addListOK && removeListOK
    }

    fun tryUpdateConfig(legacyConfigText: String, typeToken: TypeToken<*>) : Boolean {
        if(!canUpdate(legacyConfigText)) return false

        val oldConfig = gson.fromJson<T>(legacyConfigText, typeToken.type)

        PLUGIN.getResource("config.json")!!.bufferedReader().use {
            val text = it.readText()

            replaceTokens(text, oldConfig).let {newConfigText ->
                PLUGIN.dataFolder.resolve("config.json").writeText(newConfigText)
            }
        }

        return true
    }

    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting().setLenient().create()
    }
}
