package me.spartacus04.jext.config

import com.google.common.reflect.TypeToken
import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.config.legacy.*
import me.spartacus04.jext.utils.FileBind

internal object ConfigFactory {
    private fun getSerializedNames(clazz: Class<*>) =
        clazz.declaredFields.map {
            it.getAnnotation(SerializedName::class.java).value
        }

    private val legacyConfigs = listOf(
        V1Config::class.java,
        V2Config::class.java,
        V3Config::class.java,
        V4Config::class.java,
        V5Config::class.java,
        V6Config::class.java,
        V7Config::class.java
    )

    private fun updateOldConfig() {
        var text = PLUGIN.dataFolder.resolve("config.json").readText()

        val currentConfigParams = Config::class.java.declaredFields.map {
            it.getAnnotation(SerializedName::class.java).value
        }

        if(currentConfigParams.all { text.contains(it) }) return

        legacyConfigs.forEach {
            val params = getSerializedNames(it)

            val map = GSON.fromJson<HashMap<String, Any>>(text, object : TypeToken<HashMap<String, Any>>() {}.type)
            map.remove("\$schema")

            if(params.all { param -> map.containsKey(param) } && map.keys.all { key -> params.contains(key) }) {
                val instance = GSON.fromJson(text, it)
                text = instance.migrateToNext()
            }
        }

        PLUGIN.dataFolder.resolve("config.json").writeText(text)
    }

    fun createConfigObject() : Config {
        if(!PLUGIN.dataFolder.exists()) PLUGIN.dataFolder.mkdirs()

        if(PLUGIN.dataFolder.resolve("config.json").exists()) {
            updateOldConfig()
        }

        return FileBind.create(Config::class.java)
    }
}
