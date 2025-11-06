package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nWarn
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.entity.Player

class DiscCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("disc") {
        arguments.add(ArgumentDisc(plugin))
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        val disc = ctx.getArgument<Disc>(0)

        ctx.sender.inventory.addItem(disc.discItemStack)

        ctx.sender.sendI18nConfirm(plugin, "disc-command-success",
            "disc"  to disc.displayName
        )
    }
}