package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentDecimal
import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentLocation
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.jext.Jext
import me.spartacus04.jext.commands.customArgs.ArgumentDisc
import me.spartacus04.jext.discs.Disc
import org.bukkit.Location
import org.bukkit.entity.Player

class PlayAtCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("playat") {
        arguments.addAll(listOf(
            ArgumentDisc(plugin),
            ArgumentLocation()
        ))

        optionalArguments.addAll(listOf(
            ArgumentDecimal(listOf(0.5, 1.0, 2.0)),
            ArgumentDecimal(listOf(4.0, 1.0, 0.5))
        ))
    }

    override fun executePlayer(ctx: CommandContext<Player>) {
        val disc = ctx.getArgument<Disc>(0)
        val location = ctx.getArgument<Location>(1)
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

        disc.play(location, volume, pitch)

        ctx.sender.sendI18nConfirm(plugin, "playat-command-success",
            "disc" to disc.displayName,
            "location" to "(${location.blockX}, ${location.blockY}, ${location.blockZ})",
        )
    }
}