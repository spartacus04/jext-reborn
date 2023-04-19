package me.spartacus04.jext.command

import me.spartacus04.jext.jukebox.JukeboxContainer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

internal class ExecutorJukeboxGui(private val plugin: JavaPlugin) : ExecutorAdapter("jukeboxgui") {
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        JukeboxContainer.get(plugin, sender).open(sender)
        return true
    }
}