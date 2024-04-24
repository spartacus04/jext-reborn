package me.spartacus04.jext.config

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.config.legacy.*
import me.spartacus04.jext.utils.FileBind

internal object ConfigFactory {
    private fun updateOldConfig() {
        val text = PLUGIN.dataFolder.resolve("config.json").readText()
        val gson = GsonBuilder().setPrettyPrinting().setLenient().create()

        val currentConfigParams = Config::class.java.declaredFields.map {
            it.getAnnotation(SerializedName::class.java).value
        }

        if(currentConfigParams.all { text.contains(it) }) return

        if(LegacyConfig<V1Config> (
            listOf(
                "\"lang\""
            ),
            listOf(
                "\"resource-pack-decline-kick-message\"",
                "\"failed-download-kick-message\""
            )
        ) { newConfig, oldConfigData ->
            return@LegacyConfig newConfig.replace(
                "\"disable-music-overlap\": true",
                "\"disable-music-overlap\": ${!oldConfigData.ALLOW_MUSIC_OVERLAPPING}"
            ).replace(
                "\"force-resource-pack\": false",
                "\"force-resource-pack\": ${oldConfigData.FORCE_RESOURCE_PACK}"
            )
        }.tryUpdateConfig(text, object : TypeToken<V1Config>() {})) return

        if(LegacyConfig<V2Config> (
            listOf(
                "\"allow-metrics\""
            ),
            null
        ) { newConfig, oldConfigData ->
            return@LegacyConfig newConfig.replace(
                "\"force-resource-pack\": false",
                "\"force-resource-pack\": ${oldConfigData.FORCE_RESOURCE_PACK}"
            ).replace(
                "\"disable-music-overlap\": true",
                "\"disable-music-overlap\": ${!oldConfigData.ALLOW_MUSIC_OVERLAPPING}"
            ).replace(
                "\"lang\": \"auto\"",
                "\"lang\": \"${oldConfigData.LANGUAGE_FILE}\""
            )
        }.tryUpdateConfig(text, object : TypeToken<V2Config>() {})) return

        if(LegacyConfig<V3Config> (
            listOf(
                "\"jukebox-gui\"",
                "\"disc-loottables-limit\""
            ),
            null
        ) { newConfig, oldConfigData ->
            return@LegacyConfig newConfig.replace(
                "\"force-resource-pack\": false",
                "\"force-resource-pack\": ${oldConfigData.FORCE_RESOURCE_PACK}"
            ).replace(
                "\"disable-music-overlap\": true",
                "\"disable-music-overlap\": ${!oldConfigData.ALLOW_MUSIC_OVERLAPPING}"
            ).replace(
                "\"lang\": \"auto\"",
                "\"lang\": \"${oldConfigData.LANGUAGE_MODE}\""
            ).replace(
                "\"allow-metrics\": true",
                "\"allow-metrics\": ${oldConfigData.ALLOW_METRICS}"
            )
        }.tryUpdateConfig(text, object : TypeToken<V3Config>() {})) return

        if(LegacyConfig<V4Config> (
            listOf(
                "\"discs-loottables-limit\"",
                "\"fragments-loottables-limit\"",
                "\"discs-random-chance\"",
                "\"fragments-random-chance\""
            ),
            null
        ) { newConfig, oldConfigData ->
            return@LegacyConfig newConfig.replace(
                "\"force-resource-pack\": false",
                "\"force-resource-pack\": ${oldConfigData.FORCE_RESOURCE_PACK}"
            ).replace(
                "\"disable-music-overlap\": true",
                "\"disable-music-overlap\": ${!oldConfigData.ALLOW_MUSIC_OVERLAPPING}"
            ).replace(
                "\"lang\": \"auto\"",
                "\"lang\": \"${oldConfigData.LANGUAGE_MODE}\""
            ).replace(
                "\"allow-metrics\": true",
                "\"allow-metrics\": ${oldConfigData.ALLOW_METRICS}"
            ).replace(
                "\"jukebox-behaviour\": \"vanilla\"",
                "\"jukebox-behaviour\": \"${if(oldConfigData.JUKEBOX_GUI) "gui" else "vanilla"}\""
            )
        }.tryUpdateConfig(text, object : TypeToken<V4Config>() {})) return

        if(LegacyConfig<V5Config> (
            listOf(
                "\"jukebox-behaviour\""
            ),
            null
        ) { newConfig, oldConfigData ->
            return@LegacyConfig newConfig.replace(
                "\"force-resource-pack\": false",
                "\"force-resource-pack\": ${oldConfigData.FORCE_RESOURCE_PACK}"
            ).replace(
                "\"disable-music-overlap\": true",
                "\"disable-music-overlap\": ${!oldConfigData.ALLOW_MUSIC_OVERLAPPING}"
            ).replace(
                "\"lang\": \"auto\"",
                "\"lang\": \"${oldConfigData.LANGUAGE_MODE}\""
            ).replace(
                "\"allow-metrics\": true",
                "\"allow-metrics\": ${oldConfigData.ALLOW_METRICS}"
            ).replace(
                "\"jukebox-behaviour\": \"vanilla\"",
                "\"jukebox-behaviour\": \"${if(oldConfigData.JUKEBOX_GUI) "gui" else "vanilla"}\""
            ).replace(
                "\"discs-loottables-limit\": {}",
                "\"discs-loottables-limit\": ${gson.toJson(oldConfigData.DISC_LIMIT)}"
            ).replace(
                "\"fragments-loottables-limit\": {}",
                "\"fragments-loottables-limit\": ${gson.toJson(oldConfigData.FRAGMENT_LIMIT)}"
            )
        }.tryUpdateConfig(text, object : TypeToken<V5Config>() {})) return

        if(LegacyConfig<V6Config> (
            listOf(
                "\"jukebox-gui-style\"",
                "\"jukebox-gui-size\"",
                "\"check-for-updates\"",
                "\"enable-resource-pack-host\"",
                "\"web-interface-api-enabled\"",
                "\"web-interface-port\"",
                "\"web-interface-password\""
            ),
            listOf(
                "\"allow-music-overlapping\"",
                "\"ignore-failed-download\"",
                "\"discs-random-chance\"",
                "\"fragments-random-chance\"",
            )
        ) { newConfig, oldConfigData ->
            return@LegacyConfig newConfig.replace(
                "\"force-resource-pack\": false",
                "\"force-resource-pack\": ${oldConfigData.FORCE_RESOURCE_PACK}"
            ).replace(
                "\"disable-music-overlap\": true",
                "\"disable-music-overlap\": ${!oldConfigData.ALLOW_MUSIC_OVERLAPPING}"
            ).replace(
                "\"lang\": \"auto\"",
                "\"lang\": \"${oldConfigData.LANGUAGE_MODE}\""
            ).replace(
                "\"allow-metrics\": true",
                "\"allow-metrics\": ${oldConfigData.ALLOW_METRICS}"
            ).replace(
                "\"jukebox-behaviour\": \"vanilla\"",
                "\"jukebox-behaviour\": \"${if(oldConfigData.JUKEBOX_BEHAVIOUR == "legacy-gui") "gui" else oldConfigData.JUKEBOX_BEHAVIOUR}\""
            ).replace(
                "\"discs-loottables-limit\": {}",
                "\"discs-loottables-limit\": ${gson.toJson(oldConfigData.DISC_LIMIT)}"
            ).replace(
                "\"fragments-loottables-limit\": {}",
                "\"fragments-loottables-limit\": ${gson.toJson(oldConfigData.FRAGMENT_LIMIT)}"
            )
        }.tryUpdateConfig(text, object : TypeToken<V6Config>() {})) return
    }

    fun createConfigObject() : Config {
        if(!PLUGIN.dataFolder.exists()) PLUGIN.dataFolder.mkdirs()

        if(PLUGIN.dataFolder.resolve("config.json").exists()) {
            updateOldConfig()
        }

        return FileBind.create(Config::class.java)
    }
}
