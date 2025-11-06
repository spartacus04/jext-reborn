package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentInteger
import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentPlayers
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nError
import me.spartacus04.colosseum.i18n.sendI18nInfo
import me.spartacus04.colosseum.i18n.sendI18nWarn
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FragmentGiveCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("fragmentgive") {
        arguments.addAll(listOf(
            ArgumentPlayers(false),
            ArgumentDisc(plugin)
        ))

        optionalArguments.add(
            ArgumentInteger(listOf(1, 9))
        )
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        if(plugin.serverVersion < "1.19") {
            ctx.sender.sendI18nError(plugin, "command-not-supported",
                "command" to "fragmentgive",
                "reason" to "not supported in < 1.19"
            )
            return
        }

        val players = ctx.getArgument<List<Player>>(0)
        val disc = ctx.getArgument<Disc>(1)
        val amount = if(ctx.size() > 1) {
            ctx.getArgument<Int>(2)
        } else {
            1
        }

        players.forEach {
            it.inventory.addItem(disc.fragmentItemStack!!.apply {
                this.amount = amount
            })

            it.sendI18nInfo(plugin, "fragment-received",
                "disc" to disc.displayName,
                "amount" to amount.toString()
            )
        }

        if(players.size > 1) {
            ctx.sender.sendI18nInfo(plugin, "fragment-given-multiple",
                "disc" to disc.displayName,
                "playercount" to players.size.toString(),
                "amount" to amount.toString()
            )
        } else if(players.size == 1) {
            ctx.sender.sendI18nInfo(plugin, "fragment-given",
                "disc" to disc.displayName,
                "player" to players[0].name,
                "amount" to amount.toString()
            )
        } else {
            ctx.sender.sendI18nWarn(plugin, "no-fragment-given")
        }
    }
}