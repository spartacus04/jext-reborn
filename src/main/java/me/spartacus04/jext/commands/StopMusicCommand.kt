package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentPlayers
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nInfo
import me.spartacus04.colosseum.i18n.sendI18nWarn
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StopMusicCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("stopmusic") {
        arguments.add(ArgumentPlayers(false))
        optionalArguments.add(ArgumentDisc(plugin))
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        val players = ctx.getArgument<List<Player>>(0)
        val discs = if(ctx.size() > 1) {
            listOf(ctx.getArgument<Disc>(1))
        } else {
            plugin.discs.toList()
        }

        players.forEach { player ->
            discs.forEach {
                plugin.discs.stop(player, it.namespace)
            }

            if(discs.size == 1) {
                player.sendI18nInfo(plugin, "stopped-music",
                    "name" to discs[0].displayName
                )
            } else {
                player.sendI18nInfo(plugin, "stopped-all-music",)
            }
        }

        if(players.size > 1) {
            ctx.sender.sendI18nConfirm(plugin, "stopped-music-for-multiple",
                "playercount"  to players.size.toString()
            )
        } else if (players.size == 1) {
            ctx.sender.sendI18nConfirm(plugin, "stopped-music-for",
                "player" to players[0].name
            )
        } else {
            ctx.sender.sendI18nWarn(plugin, "no-players-stopped-music")
        }

    }
}