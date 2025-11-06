package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.jext.Jext
import me.spartacus04.jext.utils.JextMetrics
import org.bukkit.command.CommandSender

class ReloadCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("jextreload") {
        subCommandName = "reload"
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        plugin.config.read()
        plugin.discs.reloadDiscs()
        plugin.integrations.reloadDefaultIntegrations()
        JextMetrics.reloadMetrics()
        plugin.webServer.reload()
        plugin.geyserManager.reloadGeyser()

        ctx.sender.sendI18nConfirm(plugin, "reloaded")
    }
}