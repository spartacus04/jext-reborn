package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentInteger
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nError
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.entity.Player

internal class FragmentCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("fragment") {
        arguments.add(ArgumentDisc(plugin))
        optionalArguments.add(ArgumentInteger(listOf(1, 9)))
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        if(plugin.serverVersion < "1.19") {
            ctx.sender.sendI18nError(plugin, "command-not-supported",
                "command" to "fragment",
                "reason" to "not supported in < 1.19"
            )
            return
        }

        val disc = ctx.getArgument<Disc>(0)
        val amount = if(ctx.size() > 1) {
            ctx.getArgument<Int>(1)
        } else {
            1
        }

        ctx.sender.inventory.addItem(disc.fragmentItemStack)

        ctx.sender.sendI18nConfirm(plugin, "fragment-command-success",
            "disc" to disc.displayName,
            "amount" to amount.toString()
        )
    }
}