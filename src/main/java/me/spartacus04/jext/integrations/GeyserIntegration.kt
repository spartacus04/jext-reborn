package me.spartacus04.jext.integrations

import me.spartacus04.jext.config.ConfigData.Companion.DISCS
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.GeyserApi
import org.geysermc.geyser.api.event.EventRegistrar
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent
import org.geysermc.geyser.api.item.custom.CustomItemData
import org.geysermc.geyser.api.item.custom.CustomItemOptions

/**
 * This class is used to register and manage the Geyser integration.
 *
 * @constructor Creates a new GeyserIntegration object.
 *
 * @param plugin The plugin instance
 */
internal class GeyserIntegration(plugin: JavaPlugin) : EventRegistrar {
    private var discs : List<CustomItemData>
    init {
        GeyserApi.api().eventBus().register(this, plugin)

         discs = DISCS.map {
            val itemOptions = CustomItemOptions.builder()
                .customModelData(it.MODEL_DATA)
                .build()

            CustomItemData.builder()
                .icon(it.DISC_NAMESPACE)
                .name(it.DISC_NAMESPACE)
                .customItemOptions(itemOptions)
                .build()
        }
    }

    /**
     * Function that registers the custom items.
     *
     * @param event The event parameter represents the GeyserDefineCustomItemsEvent event.
     */
    @Subscribe
    private fun onGeyserInit(event : GeyserDefineCustomItemsEvent) {
        discs.forEach {
            event.register("minecraft:music_disc_11", it)
        }
    }

    /**
     * Function that checks if a player is a Bedrock player.
     *
     * @param player The player parameter represents the player to check.
     * @return Returns true if the player is a Bedrock player, false otherwise.
     */
    fun isBedrockPlayer(player: Player) : Boolean {
        return GeyserApi.api().isBedrockPlayer(player.uniqueId)
    }
}