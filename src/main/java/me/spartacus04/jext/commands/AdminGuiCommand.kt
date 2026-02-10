package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.jext.Jext
import me.spartacus04.jext.gui.AdminGui
import org.bukkit.entity.Player

internal class AdminGuiCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("jextadmingui") {
        subCommandName = "admingui"
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        AdminGui.buildAndOpen(plugin, ctx.sender)
    }
}