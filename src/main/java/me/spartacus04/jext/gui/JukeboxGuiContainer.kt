package me.spartacus04.jext.gui

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.LANG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.gui.items.ScrollDownItem
import me.spartacus04.jext.gui.items.ScrollUpItem
import me.spartacus04.jext.integrations.unique.GeyserIntegration.Companion.GEYSER
import me.spartacus04.jext.language.LanguageManager.Companion.BEDROCK_NOT_SUPPORTED
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent
import xyz.xenondevs.invui.inventory.event.PlayerUpdateReason
import xyz.xenondevs.invui.inventory.event.UpdateReason
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window
import java.util.*
import kotlin.collections.HashMap

class JukeboxGuiContainer {
    private val player: Player
    private val id: String
    private val block: Block?

    /**
     * Create a new jukebox container for a player
     */
    constructor(player: Player) {
        this.player = player
        this.block = null
        this.id = player.uniqueId.toString()

        mergedConstructor()
    }

    /**
     * Create a new jukebox container for a block
     */
    constructor(player: Player, block: Block) {
        this.player = player
        this.block = block
        this.id = "${block.location.world!!.name}:${block.location.blockX}:${block.location.blockY}:${block.location.blockZ}"

        mergedConstructor()
    }

    /**
     * The function `playDisc` plays a music disc, sets the itemstack as playing, and sets a timer to revert the changes
     *
     * @param event The event parameter is of type ItemPreUpdateEvent. It is an event that is triggered before an item in
     * an inventory is updated.
     */
    private fun playDisc(event: ItemPreUpdateEvent) {
        event.inventory.setItem(UpdateReason.SUPPRESSED, event.slot, event.previousItem!!.clone().apply {
            addUnsafeEnchantment(Enchantment.MENDING, 1)

            itemMeta = itemMeta!!.apply {
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
        })

        val delay = if(Disc.isCustomDisc(event.previousItem!!)) {
            val disc = Disc.fromItemstack(event.previousItem!!)!!

            if(block != null) {
                disc.play(block.location)
            } else {
                disc.play(player)
            }

            disc.duration
        } else {
            if(block != null) {
                block.location.world?.playSound(block.location, SOUND_MAP[event.previousItem!!.type]!!.sound, SoundCategory.RECORDS, 1f, 1f)
            } else {
                player.playSound(player.location, SOUND_MAP[event.previousItem!!.type]!!.sound, SoundCategory.RECORDS, 500f, 1f)
            }

            SOUND_MAP[event.previousItem!!.type]!!.duration
        }

        if(delay == -1) {
            return
        }

        timerMap[id] = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    event.inventory.setItem(UpdateReason.SUPPRESSED, playingMap[id]!!, event.previousItem!!.clone().apply {
                        removeEnchantment(Enchantment.MENDING)

                        itemMeta = itemMeta!!.apply {
                            removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                        }
                    })

                    playingMap[id] = -1

                }
            }, delay.toLong() * 1000)
        }
    }

    /**
     * The function `stopDisc` cancels the timer for a disc, sets the itemstack as not playing, and stops the disc.
     *
     * @param event The parameter "event" is of type ItemPreUpdateEvent.
     */
    private fun stopDisc(event: ItemPreUpdateEvent) {
        if(timerMap.containsKey(id)) {
            timerMap[id]!!.cancel()
            timerMap.remove(id)
        }

        if(playingMap[id]!! != -1) {
            event.inventory.setItem(UpdateReason.SUPPRESSED, playingMap[id]!!, event.inventory.getItem(playingMap[id]!!)!!.clone().apply {
                removeEnchantment(Enchantment.MENDING)

                itemMeta = itemMeta!!.apply {
                    removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                }
            })
        }

        if(block != null) {
            Disc.stop(block.location)
        } else {
            Disc.stop(player)
        }
    }

    /**
     * The function `mergedConstructor` creates and opens a scrollable GUI window for a player, displaying their inventory
     * and allowing them to scroll through it.
     */
    private fun mergedConstructor() {
        if(GEYSER?.isBedrockPlayer(player) == true) {
            player.sendMessage(BEDROCK_NOT_SUPPORTED)
            return
        }

        if(!playingMap.containsKey(id)) {
            playingMap[id] = -1
        }

        val inv = getInv(id)

        inv.setPreUpdateHandler(this::itemPreUpdateHandler)
        inv.setPostUpdateHandler(this::itemPostUpdateHandler)

        val gui = GuiBuilder().buildGui(player, inv)

        val window = Window.single()
            .setViewer(player)
            .setTitle(LANG.getKey(player, "jukebox"))
            .setGui(gui)
            .build()

        window.open()
    }

    /**
     * The function `itemPreUpdateHandler` handles various events related to updating the jukebox gui, including
     * cancelling certain actions and performing specific actions based on the event type.
     *
     * @param event The event parameter is of type ItemPreUpdateEvent. It is an event that is triggered before an item is
     * updated in an inventory.
     */
    private fun itemPreUpdateHandler(event: ItemPreUpdateEvent) {
        if((event.isAdd || event.isSwap) && event.newItem != null && !event.newItem!!.type.isRecord) {
            event.isCancelled = true
            return
        }

        if(event.updateReason == null || event.updateReason !is PlayerUpdateReason) return
        val updateReason = event.updateReason as PlayerUpdateReason

        if(updateReason.event !is InventoryClickEvent) return
        val mcevent = updateReason.event as InventoryClickEvent

        if(mcevent.isLeftClick) {
            if(event.isRemove && event.slot == playingMap[id]!!) {
                event.isCancelled = true
                stopDisc(event)
                playingMap[id] = -1
            }

            return
        }

        if(mcevent.isRightClick && !event.isRemove) return

        event.isCancelled = true

        stopDisc(event)

        if(playingMap[id]!! != -1 && playingMap[id]!! == event.slot) {
            playingMap[id] = -1
        } else {
            playingMap[id] = event.slot
            playDisc(event)
        }
    }

    /**
     * The function `itemPostUpdateHandler` saves the gui if an item is added, swapped, or removed.
     *
     * @param event The event parameter is of type ItemPostUpdateEvent, which is an event object that contains information
     * about the item post update event.
     */
    private fun itemPostUpdateHandler(event: ItemPostUpdateEvent) {
        if(event.isAdd || event.isSwap || event.isRemove) {
            save()
        }
    }

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()

        private val inventories = HashMap<String, VirtualInventory>()
        private val playingMap = HashMap<String, Int>()
        private val timerMap = HashMap<String, Timer>()

        /**
         * The function `getInv` returns a `VirtualInventory` object based on the given `id`, creating a new one if it
         * doesn't exist in the `inventories` map.
         *
         * @param id The `id` parameter is a string that represents the identifier of a virtual inventory.
         * @return The function `getInv` returns a `VirtualInventory` object.
         */
        fun getInv(id: String): VirtualInventory {
            if(inventories.containsKey(id)) {
                return inventories[id]!!
            } else if(inventories.containsKey(id.replace(":", ""))) {
                // Fixes a bug introduced in older versions where two or more jukeboxes with the same id are loaded
                inventories[id] = inventories[id.replace(":", "")]!!
                inventories.remove(id.replace(":", ""))
                return inventories[id]!!
            }

            val inv = VirtualInventory(CONFIG.GUI_SIZE)
            inventories[id] = inv

            return inv
        }

        /**
         * The function `loadFromFile()` reads data from a file, converts it into a HashMap, and populates inventories with
         * the retrieved data.
         *
         * @return In the given code, the `loadFromFile()` function does not have a return type specified. Therefore, it is
         * returning `Unit` by default.
         */
        internal fun loadFromFile() {
            val typeToken = object : TypeToken<HashMap<String, HashMap<Int, JukeboxEntry>>>() {}.type
            val file = PLUGIN.dataFolder.resolve(".savedata")

            if (inventories.isNotEmpty()) {
                save()
            }

            if (!file.exists()) {
                file.createNewFile()
            } else {
                val text = file.readText()

                val data = gson.fromJson<HashMap<String, HashMap<Int, JukeboxEntry>>>(text, typeToken) ?: return

                data.forEach { (id, itemMap) ->
                    val inv = getInv(id)

                    itemMap.forEach { (slot, entry) ->
                        inv.apply {
                            setItem(UpdateReason.SUPPRESSED, slot, entry.toItemStack())
                        }
                    }

                    inventories[id] = inv
                }

                save()
            }
        }

        /**
         * The function saves inventory data to a file in JSON format.
         */
        private fun save() {
            val file = PLUGIN.dataFolder.resolve(".savedata")

            if(!file.exists()) {
                file.createNewFile()
            }

            val data = HashMap<String, HashMap<Int, JukeboxEntry>>()

            inventories.forEach {
                data[it.key] = HashMap()

                it.value.items.forEachIndexed { index, itemStack ->
                    if(itemStack == null) return@forEachIndexed

                    if(Disc.isCustomDisc(itemStack)) {
                        val container = Disc.fromItemstack(itemStack)!!
                        data[it.key]!![index] = JukeboxEntry("jext", container.namespace)
                    } else {
                        data[it.key]!![index] = JukeboxEntry("minecraft", itemStack.type.name)
                    }
                }
            }

            file.writeText(gson.toJson(data))
        }

        /**
         * The function "destroyJukebox" removes a jukebox from a specific location and returns a list of items that were
         * inside the jukebox.
         *
         * @param location The `location` parameter is of type `Location`. It represents the location of the jukebox that
         * needs to be destroyed.
         * @return The function `destroyJukebox` returns a list of `ItemStack` objects.
         */
        fun destroyJukebox(location: Location): List<ItemStack> {
            val id = "${location.world!!.name}:${location.blockX}:${location.blockY}:${location.blockZ}"

            playingMap.remove(id)
            timerMap.remove(id)

            val inv = inventories.remove(id) ?: return emptyList()

            save()
            return inv.items.filterNotNull()

        }
    }
}