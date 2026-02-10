package me.spartacus04.jext.commands

import me.spartacus04.colosseum.commandHandling.argument.arguments.ArgumentString
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nError
import me.spartacus04.colosseum.i18n.sendI18nInfo
import me.spartacus04.jext.Jext
import org.bukkit.command.CommandSender

internal class WebUICommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("jextwebui") {
        subCommandName = "webui"

        optionalArguments.add(ArgumentString(listOf("config", "docs", "discs"), false))
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        if(!plugin.config.WEB_INTERFACE_API_ENABLED) {
            return ctx.sender.sendI18nError(plugin, "webui-disabled")
        }

        val args = if(ctx.size() > 0) ctx.getArgument<String>(0) else ""

        val page = if(args.isEmpty()) "" else when(args) {
            "config" -> "config"
            "docs" -> "documentation"
            else -> ""
        }

        val ip = plugin.baseUrl.getUrl(ctx.sender)

        ctx.sender.sendI18nInfo(plugin, "webui",
            "url" to "https://spartacus04.github.io/jext-reborn/${page}?c=c&ip=${ip}&port=${plugin.config.WEB_INTERFACE_PORT}"
        )
    }
}