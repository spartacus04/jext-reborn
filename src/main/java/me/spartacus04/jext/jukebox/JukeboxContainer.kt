package me.spartacus04.jext.jukebox

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.config.ConfigData.Companion.LANG
import me.spartacus04.jext.config.ConfigData.Companion.PLUGIN
import me.spartacus04.jext.disc.DiscContainer
import me.spartacus04.jext.disc.DiscContainer.Companion.SOUND_MAP
import me.spartacus04.jext.disc.DiscPlayer
import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
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

class JukeboxContainer {
    private val player: Player
    private val id: String
    private val block: Block?

    constructor(player: Player) {
        this.player = player
        this.block = null
        this.id = player.uniqueId.toString()

        mergedConstructor()
    }

    constructor(player: Player, block: Block) {
        this.player = player
        this.block = block
        this.id = "${block.location.world!!.name}:${block.location.blockX}:${block.location.blockY}:${block.location.blockZ}"

        mergedConstructor()
    }

    private fun playDisc(event: ItemPreUpdateEvent) {
        event.inventory.setItem(UpdateReason.SUPPRESSED, event.slot, event.previousItem!!.clone().apply {
            addUnsafeEnchantment(Enchantment.MENDING, 1)

            itemMeta = itemMeta!!.apply {
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
        })

        val delay = try {
            val disc = DiscContainer(event.previousItem!!)

            if(block != null) {
                disc.play(block.location)
            } else {
                disc.play(player)
            }

            disc.duration
        } catch (_: IllegalStateException) {
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

    private fun stopDisc(event: ItemPreUpdateEvent) {
        if(timerMap.containsKey(id)) {
            timerMap[id]!!.cancel()
            timerMap.remove(id)
        }

        if(playingMap[id]!! != -1) {
            event.inventory.setItem(UpdateReason.SUPPRESSED, playingMap[id]!!, event.previousItem!!.clone().apply {
                removeEnchantment(Enchantment.MENDING)

                itemMeta = itemMeta!!.apply {
                    removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                }
            })
        }

        if(block != null) {
            DiscPlayer.stop(block.location)
        } else {
            DiscPlayer.stop(player)
        }
    }

    private fun mergedConstructor() {
        if(!playingMap.containsKey(id)) {
            playingMap[id] = -1
        }

        val inv = getInv(id)

        inv.setPreUpdateHandler(this::itemPreUpdateHandler)
        inv.setPostUpdateHandler(this::itemPostUpdateHandler)

        val gui = ScrollGui.inventories()
            .setStructure(
                "x x x x x x x x u",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x #",
                "x x x x x x x x d"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('u', ScrollUpItem(player))
            .addIngredient('d', ScrollDownItem(player))
            .addIngredient('#', border)
            .setContent(listOf(inv))

        val window = Window.single()
            .setViewer(player)
            .setTitle(LANG.getKey(player, "jukebox"))
            .setGui(gui)
            .build()

        window.open()
    }

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

    private fun itemPostUpdateHandler(event: ItemPostUpdateEvent) {
        if(event.isAdd || event.isSwap || event.isRemove) {
            save()
        }
    }

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()
        private val border = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))

        private val inventories = HashMap<String, VirtualInventory>()
        private val playingMap = HashMap<String, Int>()
        private val timerMap = HashMap<String, Timer>()

        fun getInv(id: String): VirtualInventory {
            if(inventories.containsKey(id)) {
                return inventories[id]!!
            } else if(inventories.containsKey(id.replace(":", ""))) {
                // Fixes a bug introduced in older versions where two or more jukeboxes with the same id are loaded
                inventories[id] = inventories[id.replace(":", "")]!!
                inventories.remove(id.replace(":", ""))
                return inventories[id]!!
            }

            val inv = VirtualInventory(96)
            inventories[id] = inv

            return inv
        }

        fun loadFromFile() {
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

                    try {
                        val container = DiscContainer(itemStack)
                        data[it.key]!![index] = JukeboxEntry("jext", container.namespace)
                    } catch (_: IllegalStateException) {
                        data[it.key]!![index] = JukeboxEntry("minecraft", itemStack.type.name)
                    }
                }
            }

            file.writeText(gson.toJson(data))
        }
    }
}