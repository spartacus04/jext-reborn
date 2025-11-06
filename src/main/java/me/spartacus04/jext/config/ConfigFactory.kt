package me.spartacus04.jext.config

import com.google.common.reflect.TypeToken
import com.google.gson.annotations.SerializedName
import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.jext.config.legacy.*
import me.spartacus04.jext.utils.JextFileBind

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

    private fun updateOldConfig(plugin: ColosseumPlugin) {
        var text = plugin.dataFolder.resolve("config.json").readText()

        val currentConfigParams = Config::class.java.declaredFields.map {
            it.getAnnotation(SerializedName::class.java).value
        }

        if(currentConfigParams.all { text.contains(it) }) return

        legacyConfigs.forEach {
            val params = getSerializedNames(it)

            val map = plugin.gson.fromJson<HashMap<String, Any>>(text, object : TypeToken<HashMap<String, Any>>() {}.type)
            map.remove("\$schema")

            if(params.all { param -> map.containsKey(param) } && map.keys.all { key -> params.contains(key) }) {
                val instance = plugin.gson.fromJson(text, it)
                text = instance.migrateToNext(plugin)
            }
        }

        plugin.dataFolder.resolve("config.json").writeText(text)
    }

    fun createConfigObject(plugin: ColosseumPlugin) : Config {
        if(!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

        if(plugin.dataFolder.resolve("config.json").exists()) {
            updateOldConfig(plugin)
        }

        return JextFileBind.create(Config::class.java)
    }
}
