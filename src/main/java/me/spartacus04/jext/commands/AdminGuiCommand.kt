package me.spartacus04.jext.commands

import me.spartacus04.colosseum.ColosseumPlugin
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.jext.gui.AdminGui
import org.bukkit.entity.Player

class AdminGuiCommand(plugin: ColosseumPlugin) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("jextadmingui") {
        subCommandName = "admingui"
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        AdminGui.open(ctx.sender)
    }
}