package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.jukebox.JukeboxContainer
import org.bukkit.entity.Player

internal class ExecutorJukeboxGui : ExecutorAdapter("jukeboxgui") {
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        JukeboxContainer(sender)
        return true
    }
}