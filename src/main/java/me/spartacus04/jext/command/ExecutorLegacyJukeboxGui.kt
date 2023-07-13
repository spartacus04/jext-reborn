package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.jukebox.legacy.LegacyJukeboxContainer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

@Suppress("deprecation")
@ApiStatus.ScheduledForRemoval(inVersion = "1.3")
@Deprecated("This is part of the legacy jukebox gui system. It's going to be removed in the next major update.")
class ExecutorLegacyJukeboxGui(private val plugin: JavaPlugin) : ExecutorAdapter("legacyjukeboxgui") {
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        LegacyJukeboxContainer.get(plugin, sender).open(sender)
        return true
    }
}