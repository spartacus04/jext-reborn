package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentInteger
import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentPlayers
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nError
import me.spartacus04.colosseum.i18n.sendI18nInfo
import me.spartacus04.colosseum.i18n.sendI18nWarn
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal class DiscGiveCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("discgive") {
        arguments.addAll( listOf(
            ArgumentPlayers(false),
            ArgumentDisc(plugin),
        ))

        optionalArguments.add(
            ArgumentInteger(listOf(1))
        )
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        val players = ctx.getArgument<List<Player>>(0)
        val disc = ctx.getArgument<Disc>(1)

        val amount = if(ctx.size() > 2) {
            ctx.getArgument<Int>(2)
        } else {
            1
        }

        players.forEach {
            for(i in 1..amount) {
                it.inventory.addItem(disc.discItemStack)
            }

            it.sendI18nInfo(plugin, "disc-received",
                "disc" to disc.displayName
            )
        }

        if(players.size > 1) {
            ctx.sender.sendI18nConfirm(plugin, "disc-given-multiple",
                "disc" to disc.displayName,
                "playercount" to players.size.toString()
            )
        } else if(players.size == 1) {
            ctx.sender.sendI18nConfirm(plugin, "disc-given",
                "disc" to disc.displayName,
                "player" to players[0].name
            )
        } else {
            ctx.sender.sendI18nWarn(plugin, "no-disc-given")
        }
    }
}