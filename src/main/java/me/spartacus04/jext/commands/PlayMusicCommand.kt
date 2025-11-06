package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentDecimal
import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentPlayers
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nError
import me.spartacus04.colosseum.i18n.sendI18nInfo
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayMusicCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("playmusic") {
        subCommandName = "play"

        arguments.addAll(listOf(
            ArgumentPlayers(false),
            ArgumentDisc(plugin)
        ))

        optionalArguments.addAll(listOf(
            ArgumentDecimal(listOf(0.5, 1.0, 1.5)),
            ArgumentDecimal(listOf(4.0, 1.0, 0.5))
        ))
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        val players = ctx.getArgument<List<Player>>(0)
        val disc = ctx.getArgument<Disc>(1)
        val pitch = if(ctx.size() > 2) {
            ctx.getArgument<Double>(2).toFloat()
        } else {
            1.0f
        }
        val volume = if(ctx.size() > 3) {
            ctx.getArgument<Double>(3).toFloat()
        } else {
            1.0f
        }

        players.forEach {
            disc.play(it, volume, pitch)

            it.sendI18nInfo(plugin, "music-now-playing",
                "name" to disc.displayName
            )
        }

        if(players.size > 1) {
            ctx.sender.sendI18nConfirm(plugin, "played-music-to-multiple",
                "name" to disc.displayName,
                "playercount" to players.size.toString()
            )
        } else if(players.size == 1) {
            ctx.sender.sendI18nConfirm(plugin, "played-music-to",
                "name" to disc.displayName,
                "player" to players[0].name
            )
        } else {
            ctx.sender.sendI18nError(plugin, "no-music-played")
        }
    }
}