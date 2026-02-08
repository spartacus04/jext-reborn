package me.spartacus04.jext.gui

import com.google.gson.reflect.TypeToken
import me.spartacus04.colosseum.gui.Gui
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventory
import me.spartacus04.colosseum.gui.virtualInventory.VirtualInventoryClickEvent
import me.spartacus04.jext.Jext
import me.spartacus04.jext.Jext.Companion.INSTANCE
import me.spartacus04.jext.discs.Disc
import me.spartacus04.jext.utils.Constants.SOUND_MAP
import org.bukkit.Location
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.HashMap
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.component1
import kotlin.collections.component2

class JukeboxGui(size: Int, val targetBlock: Block? = null, val id: String, val plugin: Jext) : VirtualInventory(size) {
    init {
        if(!playingMap.containsKey(id)) {
            playingMap[id] = -1
        }
    }

    override fun onPreUpdateEvent(clickEvent: VirtualInventoryClickEvent) {
        val isBedrock = plugin.geyserManager.isBedrockPlayer(clickEvent.player)

        val addTakeClick = if(isBedrock) {
            !clickEvent.isShiftClick
        } else {
            clickEvent.isRightClick
        }

        // If the player tries to add a non-record item, cancel the event
        // Also, if the player tries to add a record with the play/stop click, simply ignore it
        if(clickEvent.cursor != null && !clickEvent.cursor!!.type.isRecord) {
            clickEvent.isCancelled = true
            return
        } else if(clickEvent.cursor != null && clickEvent.cursor!!.type.isRecord && !addTakeClick) {
            return
        }

        if(addTakeClick) {
            // if I'm removing an item, and the slot is currently playing, stop the music
            if(clickEvent.virtualSlot == playingMap[id]) {
                clickEvent.isCancelled = true
                stopDisc(clickEvent)
                playingMap[id] = -1
            }
        } else {
            if(
                (isBedrock && clickEvent.action != InventoryAction.MOVE_TO_OTHER_INVENTORY) ||
                (!isBedrock && clickEvent.isRightClick)
            ) {
                return
            }

            clickEvent.isCancelled = true

            stopDisc(clickEvent)

            if(playingMap[id]!! != -1 && playingMap[id]!! == clickEvent.virtualSlot) {
                playingMap[id] = -1
            } else {
                playingMap[id] = clickEvent.virtualSlot
                playDisc(clickEvent)
            }
        }
    }

    private fun playDisc(event: VirtualInventoryClickEvent) {
        val itemStack = get(event.virtualSlot) ?: return

        set(event.virtualSlot, itemStack.clone().apply {
            addUnsafeEnchantment(Enchantment.MENDING, 1)

            itemMeta = itemMeta!!.apply {
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
        })

        val delay = if(Disc.isCustomDisc(itemStack)) {
            val disc = Disc.fromItemstack(itemStack)!!

            if(targetBlock != null) {
                disc.play(targetBlock.location)
            } else {
                disc.play(event.player)
            }

            disc.duration
        } else {
            if(targetBlock != null) {
                targetBlock.location.world?.playSound(
                    targetBlock.location,
                    SOUND_MAP[itemStack.type]!!.sound,
                    SoundCategory.RECORDS, 1f, 1f
                )
            } else {
                event.player.playSound(
                    event.player.location,
                    SOUND_MAP[itemStack.type]!!.sound,
                    SoundCategory.RECORDS, 500f, 1f
                )
            }

            SOUND_MAP[itemStack.type]!!.duration
        }

        if(delay == -1) {
            return
        }

        timerMap[id] = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    val slot = playingMap[id] ?: return
                    set(slot, get(slot)!!.clone().apply {
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

    private fun stopDisc(event: VirtualInventoryClickEvent) {
        if(timerMap.containsKey(id)) {
            timerMap[id]!!.cancel()
            timerMap.remove(id)
        }

        if(playingMap[id]!! != -1) {
            val slot = playingMap[id]!!

            set(
                slot,
                get(slot)!!.clone().apply {
                    removeEnchantment(Enchantment.MENDING)

                    itemMeta = itemMeta!!.apply {
                        removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                    }
                }
            )
        }

        if(targetBlock != null) {
            plugin.discs.stop(targetBlock.location)
        } else {
            plugin.discs.stop(event.player)
        }
    }

    override fun onPostUpdateEvent(clickEvent: VirtualInventoryClickEvent) {
        if(clickEvent.action != InventoryAction.NOTHING) {
            save()
        }
    }

    companion object {
        private val inventories = HashMap<String, JukeboxGui>()
        private val playingMap = HashMap<String, Int>()
        private val timerMap = HashMap<String, Timer>()

        private val saveFile = INSTANCE.dataFolder.resolve(".savedata")

        fun buildAndOpen(player: Player, plugin: Jext): Gui {
            val inv = getInv(player.uniqueId.toString())
            return buildAndOpen(player, inv, plugin)
        }

        fun buildAndOpen(player: Player, block: Block, plugin: Jext): Gui {
            val inv = getInv(
                "${block.location.world!!.name}:${block.location.blockX}:${block.location.blockY}:${block.location.blockZ}",
            )
            return buildAndOpen(player, inv, plugin)
        }

        fun buildAndOpen(player: Player, inv: JukeboxGui, plugin: Jext): Gui {
            return Gui.buildAndOpen(plugin) {
                setTitle(
                    plugin.i18nManager!![player, "jukebox"]!!
                )
                setJextStructure(
                    player,
                    inv
                )
            }
        }

        @Suppress("MemberVisibilityCanBePrivate")
        /**
         * The function `getInv` returns a `JukeboxGui` object based on the given `id`, creating a new one if it
         * doesn't exist in the `inventories` map.
         *
         * @param id The `id` parameter is a string that represents the identifier of a virtual inventory.
         * @return The function `getInv` returns a `JukeboxGui` object.
         */
        fun getInv(id: String): JukeboxGui {
            if(inventories.containsKey(id)) {
                return inventories[id]!!
            } else if(inventories.containsKey(id.replace(":", ""))) {
                // Fixes a bug introduced in older versions where two or more jukeboxes with the same id are loaded
                inventories[id] = inventories[id.replace(":", "")]!!
                inventories.remove(id.replace(":", ""))
                return inventories[id]!!
            }

            val block = if(id.contains(":")) {
                val parts = id.split(":")
                INSTANCE.server.getWorld(parts[0])!!
                    .getBlockAt(parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
            } else {
                null
            }

            val inv = JukeboxGui(INSTANCE.config.GUI_SIZE, block, id, INSTANCE)
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

                val data = INSTANCE.gson.fromJson<HashMap<String, HashMap<Int, JukeboxEntry>>>(text, typeToken) ?: return

                data.forEach { (id, itemMap) ->
                    val inv = getInv(id)

                    inv.itemStacks.fill(null)

                    itemMap.forEach { (slot, entry) ->
                        inv.apply {
                            set(slot, entry.toItemStack())
                        }
                    }

                    inv.refreshAll()
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
                if (inv.itemStacks.all { it == null }) return@forEach

                data[key] = HashMap()

                inv.itemStacks.forEachIndexed { index, itemStack ->
                    if (itemStack == null) return@forEachIndexed

                    if (Disc.isCustomDisc(itemStack)) {
                        try {
                            val container = Disc.fromItemstack(itemStack)!!
                            data[key]!![index] = JukeboxEntry(container.sourceId, container.namespace)
                        } catch (_: NullPointerException) {
                            val stack = arrayListOf(
                                SOUND_MAP.keys.map { disc -> ItemStack(disc) },
                                INSTANCE.discs.map { disc -> disc.discItemStack }
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

            saveFile.writeText(INSTANCE.gson.toJson(data))
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
            return inv.itemStacks.filterNotNull()

        }
    }
}