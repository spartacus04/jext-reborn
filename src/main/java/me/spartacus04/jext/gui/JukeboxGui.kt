package me.spartacus04.jext.gui

import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.JextState.LANG
import me.spartacus04.jext.JextState.PLUGIN
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.event.ItemPostUpdateEvent
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent
import xyz.xenondevs.invui.inventory.event.PlayerUpdateReason
import xyz.xenondevs.invui.inventory.event.UpdateReason
import java.util.*

internal class JukeboxGui : BaseGui {
    private constructor(player: Player, inventory: VirtualInventory, inventoryName: String) : super(player, inventory, inventoryName)

    private constructor(player: Player, block: Block, inventory: VirtualInventory, inventoryName: String) : super(player, block, inventory, inventoryName)


    override fun onInit() {
        if(!playingMap.containsKey(inventoryId)) {
            playingMap[inventoryId] = -1
        }
    }

    override fun onItemPreUpdate(event: ItemPreUpdateEvent) {
        if((event.isAdd || event.isSwap) && event.newItem != null && !event.newItem!!.type.isRecord) {
            event.isCancelled = true
            return
        }

        if(event.updateReason == null || event.updateReason !is PlayerUpdateReason) return
        val updateReason = event.updateReason as PlayerUpdateReason

        if(updateReason.event !is InventoryClickEvent) return
        val mcevent = updateReason.event as InventoryClickEvent

        val isAltClick = if(isBedrock) {
            !mcevent.isShiftClick
        } else {
            mcevent.isRightClick
        }

        if(event.isAdd && event.newItem != null && event.newItem!!.type.isRecord && !isAltClick) {
            return
        }

        if(isAltClick) {
            if(event.isRemove && event.slot == playingMap[inventoryId]!!) {
                event.isCancelled = true
                stopDisc(event)
                playingMap[inventoryId] = -1
            }

            return
        }

        if(
            (isBedrock && !event.isRemove) ||
            (!isBedrock && mcevent.isRightClick && !event.isRemove)
        ) return

        event.isCancelled = true

        stopDisc(event)

        if(playingMap[inventoryId]!! != -1 && playingMap[inventoryId]!! == event.slot) {
            playingMap[inventoryId] = -1
        } else {
            playingMap[inventoryId] = event.slot
            playDisc(event)
        }
    }

    override fun onItemPostUpdate(event: ItemPostUpdateEvent) {
        if(event.isAdd || event.isSwap || event.isRemove) {
            save()
        }
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

            if(targetBlock != null) {
                disc.play(targetBlock.location)
            } else {
                disc.play(targetPlayer)
            }

            disc.duration
        } else {
            if(targetBlock != null) {
                targetBlock.location.world?.playSound(targetBlock.location, SOUND_MAP[event.previousItem!!.type]!!.sound, SoundCategory.RECORDS, 1f, 1f)
            } else {
                targetPlayer.playSound(targetPlayer.location, SOUND_MAP[event.previousItem!!.type]!!.sound, SoundCategory.RECORDS, 500f, 1f)
            }

            SOUND_MAP[event.previousItem!!.type]!!.duration
        }

        if(delay == -1) {
            return
        }

        timerMap[inventoryId] = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    event.inventory.setItem(UpdateReason.SUPPRESSED, playingMap[inventoryId]!!, event.previousItem!!.clone().apply {
                        removeEnchantment(Enchantment.MENDING)

                        itemMeta = itemMeta!!.apply {
                            removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                        }
                    })

                    playingMap[inventoryId] = -1

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
        if(timerMap.containsKey(inventoryId)) {
            timerMap[inventoryId]!!.cancel()
            timerMap.remove(inventoryId)
        }

        if(playingMap[inventoryId]!! != -1) {
            event.inventory.setItem(
                UpdateReason.SUPPRESSED,
                playingMap[inventoryId]!!,
                event.inventory.getItem(playingMap[inventoryId]!!)!!.clone().apply {
                    removeEnchantment(Enchantment.MENDING)

                    itemMeta = itemMeta!!.apply {
                        removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                    }
                }
            )
        }

        if(targetBlock != null) {
            DISCS.stop(targetBlock.location)
        } else {
            DISCS.stop(targetPlayer)
        }
    }


    companion object {
        fun open(player: Player) = JukeboxGui(
            player,
            getInv(player.uniqueId.toString()),
            LANG.getKey(player, "jukebox")
        )

        fun open(player: Player, block: Block) = JukeboxGui(
            player,
            block,
            getInv("${block.location.world!!.name}:${block.location.blockX}:${block.location.blockY}:${block.location.blockZ}"),
            LANG.getKey(player, "jukebox"),
        )

        private val inventories = HashMap<String, VirtualInventory>()
        private val playingMap = HashMap<String, Int>()
        private val timerMap = HashMap<String, Timer>()

        private val saveFile = PLUGIN.dataFolder.resolve(".savedata")


        @Suppress("MemberVisibilityCanBePrivate")
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

            if (inventories.isNotEmpty()) {
                save()
            }

            if (!saveFile.exists()) {
                saveFile.createNewFile()
            } else {
                val text = saveFile.readText()

                val data = GSON.fromJson<HashMap<String, HashMap<Int, JukeboxEntry>>>(text, typeToken) ?: return

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
            if(!saveFile.exists()) {
                saveFile.createNewFile()
            }

            val data = HashMap<String, HashMap<Int, JukeboxEntry>>()

            inventories.forEach { (key, inv) ->
                // Skip entirely empty jukeboxes
                if (inv.items.all { it == null }) return@forEach

                data[key] = HashMap()

                inv.items.forEachIndexed { index, itemStack ->
                    if (itemStack == null) return@forEachIndexed

                    if (Disc.isCustomDisc(itemStack)) {
                        try {
                            val container = Disc.fromItemstack(itemStack)!!
                            data[key]!![index] = JukeboxEntry(container.sourceId, container.namespace)
                        } catch (e: NullPointerException) {
                            val stack = arrayListOf(
                                SOUND_MAP.keys.map { disc -> ItemStack(disc) },
                                DISCS.map { disc -> disc.discItemStack }
                            ).flatten().random()

                            if (Disc.isCustomDisc(stack)) {
                                val container = Disc.fromItemstack(stack)!!
                                data[key]!![index] = JukeboxEntry(container.sourceId, container.namespace)
                            } else {
                                data[key]!![index] = JukeboxEntry("minecraft", itemStack.type.name)
                            }
                        }
                    } else {
                        data[key]!![index] = JukeboxEntry("minecraft", itemStack.type.name)
                    }
                }
            }

            saveFile.writeText(GSON.toJson(data))
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
