package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.jext.Jext
import me.spartacus04.jext.gui.JukeboxGui
import org.bukkit.entity.Player

class JukeboxGuiCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("jukeboxgui") {
        subCommandName = "gui"
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        JukeboxGui.open(ctx.sender)
    }
}