package me.spartacus04.jext.command

import me.spartacus04.jext.command.adapter.ExecutorAdapter
import me.spartacus04.jext.jukebox.JukeboxContainer
import org.bukkit.entity.Player

internal class ExecutorJukeboxGui : ExecutorAdapter("jukeboxgui") {
    /**
     * The function "executePlayer" creates a JukeboxContainer object and returns true.
     *
     * @param sender The "sender" parameter is of type Player, which represents the player who executed the command.
     * @param args An array of strings that represents the arguments passed to the command.
     * @return a boolean value of true.
     */
    override fun executePlayer(sender: Player, args: Array<String>): Boolean {
        JukeboxContainer(sender)
        return true
    }
}