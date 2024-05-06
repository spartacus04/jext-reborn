package me.spartacus04.jext.gui

import me.spartacus04.jext.geyser.GeyserIntegration.Companion.GEYSER
import org.bukkit.block.Block
import org.bukkit.entity.Player
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent
import xyz.xenondevs.invui.window.Window

abstract class BaseGui {
    internal val targetPlayer: Player
    internal val inventoryId: String
    internal val targetBlock: Block?
    internal val isBedrock: Boolean
    abstract val inventory: VirtualInventory
    abstract val inventoryName: String

    /**
     * Create a new jukebox container for a player
     */
    constructor(player: Player) {
        this.targetPlayer = player
        this.inventoryId = player.uniqueId.toString()
        this.targetBlock = null
        this.isBedrock = try {
            GEYSER?.isBedrockPlayer(player) ?: false
        } catch (_: NoClassDefFoundError) { false }

        this.onInit()
        this.setHandlers()
        this.finalizeGui()
    }

    /**
     * Create a new jukebox container for a block
     */
    constructor(player: Player, block: Block) {
        this.targetPlayer = player
        this.inventoryId = "${block.location.world!!.name}:${block.location.blockX}:${block.location.blockY}:${block.location.blockZ}"
        this.targetBlock = block
        this.isBedrock = try {
            GEYSER?.isBedrockPlayer(player) ?: false
        } catch (_: NoClassDefFoundError) { false }

        this.onInit()
        this.setHandlers()
        this.finalizeGui()
    }

    abstract fun onItemPreUpdate(event: ItemPreUpdateEvent)

    abstract fun onItemPostUpdate(event: ItemPostUpdateEvent)

    abstract fun onBedrockItemPostUpdate(event: ItemPostUpdateEvent)

    abstract fun onBedrockItemPreUpdate(event: ItemPreUpdateEvent)

    abstract fun onInit()

    private fun setHandlers() {
        if(isBedrock) {
            inventory.setPreUpdateHandler(this::onBedrockItemPreUpdate)
            inventory.setPostUpdateHandler(this::onBedrockItemPostUpdate)
        } else {
            inventory.setPreUpdateHandler(this::onItemPreUpdate)
            inventory.setPostUpdateHandler(this::onItemPostUpdate)
        }
    }

    @SuppressWarnings
    fun finalizeGui() {
        val gui = GuiBuilder().buildGui(targetPlayer, inventory)

        val window = Window.single()
            .setViewer(targetPlayer)
            .setTitle(inventoryName)
            .setGui(gui)
            .build()

        window.open()

        inventory.notifyWindows()
    }
}