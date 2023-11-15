package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.gui.JukeboxGuiContainer
import org.bukkit.entity.Player

/**
 * ExecutorJukeboxGui is a class used to register the "jukeboxgui" command to the plugin.
 *
 * @constructor The constructor is empty because the class does not have any properties.
 */
internal class ExecutorJukeboxGui : ExecutorAdapter("jukeboxgui") {
    /**
     * The function `executePlayer` opens the jukebox gui for the sender.
     *
     * @param sender The player who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun executePlayer(sender: Player, args: Array<String>){
        JukeboxGuiContainer(sender)
    }
}