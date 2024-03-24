package me.spartacus04.jext.integrations.unique

import me.spartacus04.jext.State.DISCS
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.State.VERSION
import org.bukkit.entity.Player
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.GeyserApi
import org.geysermc.geyser.api.event.EventRegistrar
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent
import org.geysermc.geyser.api.item.custom.CustomItemData
import org.geysermc.geyser.api.item.custom.CustomItemOptions

internal class GeyserIntegration : EventRegistrar {
    private var discs : List<CustomItemData>
    private var fragments : List<CustomItemData>? = null

    init {
        GeyserApi.api().eventBus().register(this, PLUGIN)

        discs = DISCS.map {
            val itemOptions = CustomItemOptions.builder()
                .customModelData(it.discItemStack.itemMeta?.customModelData ?: 0)
                .build()

            CustomItemData.builder()
                .icon(it.namespace)
                .name(it.namespace)
                .customItemOptions(itemOptions)
                .build()
        }

        if(VERSION >= "1.19") {
            fragments = DISCS.map {
                val itemOptions = CustomItemOptions.builder()
                    .customModelData(it.fragmentItemStack?.itemMeta?.customModelData ?: 0)
                    .build()

                CustomItemData.builder()
                    .icon(it.namespace)
                    .name(it.namespace)
                    .customItemOptions(itemOptions)
                    .build()

            }
        }
    }

    @Suppress("unused")
    @Subscribe
    private fun onGeyserInit(event : GeyserDefineCustomItemsEvent) {
        discs.forEach {
            event.register("minecraft:music_disc_11", it)
        }
        fragments?.forEach {
            event.register("minecraft:disc_fragment_5", it)
        }
    }

    fun isBedrockPlayer(player: Player) : Boolean {
        return GeyserApi.api().isBedrockPlayer(player.uniqueId)
    }

    companion object {
        var GEYSER : GeyserIntegration? = null
    }
}