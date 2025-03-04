package me.spartacus04.jext.geyser

import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.JextState.VERSION
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.GeyserApi
import org.geysermc.geyser.api.event.EventRegistrar
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent
import org.geysermc.geyser.api.item.custom.CustomItemData
import org.geysermc.geyser.api.item.custom.CustomItemOptions
import java.util.UUID

internal class GeyserSpigot : EventRegistrar, GeyserMode {

    init {
        GeyserApi.api().eventBus().register(this, PLUGIN)
        GeyserApi.api().eventBus().subscribe(this, GeyserDefineCustomItemsEvent::class.java, this::onGeyserInit)
    }

    @Suppress("unused")
    @Subscribe
    private fun onGeyserInit(event : GeyserDefineCustomItemsEvent) {
        DISCS.map {
            val itemOptions = CustomItemOptions.builder()
                .customModelData(it.discItemStack.itemMeta!!.customModelData)
                .build()

            CustomItemData.builder()
                .icon("music_disc_${it.namespace}")
                .name("music_disc_${it.namespace}")
                .displayName("Music Disc")
                .customItemOptions(itemOptions)
                .build()
        }.forEach {
            event.register("minecraft:music_disc_11", it)
        }

        if(VERSION >= "1.19") {
            DISCS.map {
                val itemOptions = CustomItemOptions.builder()
                    .customModelData(it.fragmentItemStack?.itemMeta?.customModelData ?: 0)
                    .build()

                CustomItemData.builder()
                    .icon("fragment_${it.namespace}")
                    .name("fragment_${it.namespace}")
                    .displayName("Disc Fragment")
                    .customItemOptions(itemOptions)
                    .build()
            }.forEach {
                event.register("minecraft:disc_fragment_5", it)
            }
        }
    }

    override fun isBedrockPlayer(player: UUID) = GeyserApi.api().isBedrockPlayer(player)

    override fun applyResourcePack(buffer: ByteArray) {
        val geyserPlugin = PLUGIN.server.pluginManager.getPlugin("Geyser-Spigot") ?: return

        geyserPlugin.dataFolder.resolve("packs").resolve("jext_resources.mcpack").writeBytes(buffer)
    }
}