package me.spartacus04.jext.gui

import me.spartacus04.jext.Jext
import org.bukkit.block.Block
import org.bukkit.entity.Player
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent
import xyz.xenondevs.invui.window.Window

/**
 * The abstract class `BaseGui` is used to create a base gui for the plugin.
 */
abstract class BaseGui {
    internal val targetPlayer: Player
    internal val inventoryId: String
    internal val targetBlock: Block?
    internal val isBedrock: Boolean
    internal val inventory: VirtualInventory
    private val inventoryName: String
    private val plugin: Jext

    /**
     * Create a new jukebox container for a player
     */
    protected constructor(player: Player, inventory: VirtualInventory, inventoryName: String, plugin: Jext) {
        this.targetPlayer = player
        this.inventoryId = player.uniqueId.toString()
        this.targetBlock = null
        this.isBedrock = plugin.geyserManager.isBedrockPlayer(player)
        this.inventory = inventory
        this.inventoryName = inventoryName
        this.plugin = plugin

        this.onInit()
        this.setHandlers()
        this.finalizeGui()
    }

    /**
     * Create a new jukebox container for a block
     */
    protected constructor(player: Player, block: Block, inventory: VirtualInventory, inventoryName: String, plugin: Jext) {
        this.targetPlayer = player
        this.inventoryId = "${block.location.world!!.name}:${block.location.blockX}:${block.location.blockY}:${block.location.blockZ}"
        this.targetBlock = block
        this.isBedrock = plugin.geyserManager.isBedrockPlayer(player)
        this.inventory = inventory
        this.inventoryName = inventoryName
        this.plugin = plugin

        this.onInit()
        this.setHandlers()
        this.finalizeGui()
    }

    /**
     * Fires when an item is about to be updated.
     */
    abstract fun onItemPreUpdate(event: ItemPreUpdateEvent)

    /**
     * Fires when an item has been updated.
     */
    abstract fun onItemPostUpdate(event: ItemPostUpdateEvent)

    /**
     * Fires when the gui is initialized.
     */
    abstract fun onInit()

    private fun setHandlers() {
        inventory.setPreUpdateHandler(this::onItemPreUpdate)
        inventory.setPostUpdateHandler(this::onItemPostUpdate)
    }

    /**
     * Finalizes the gui.
     */
    @SuppressWarnings
    fun finalizeGui() {
        val gui = GuiBuilder().buildGui(targetPlayer, inventory, plugin)

        val window = Window.single()
            .setViewer(targetPlayer)
            .setTitle(inventoryName)
            .setGui(gui)
            .build()

        window.open()

        inventory.notifyWindows()
    }
}