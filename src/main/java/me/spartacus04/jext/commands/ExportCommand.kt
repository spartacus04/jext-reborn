package me.spartacus04.jext.commands

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spartacus04.colosseum.commandHandling.command.ColosseumCommand
import me.spartacus04.colosseum.i18n.sendI18nConfirm
import me.spartacus04.colosseum.i18n.sendI18nError
import me.spartacus04.jext.Jext
import org.bukkit.command.CommandSender

class ExportCommand(val plugin: Jext) : ColosseumCommand(plugin) {
    override val commandData = commandDescriptor("jextexport") {
        subCommandName = "export"
    }

    override fun execute(ctx: CommandContext<CommandSender>) {
        ctx.sender.sendI18nConfirm(plugin, "export-start",
            "file" to "exported.zip"
        )

        plugin.scheduler.runTaskAsynchronously {
            CoroutineScope(Dispatchers.Default).launch {
                if(plugin.assetsManager.tryExportResourcePack()) {
                    ctx.sender.sendI18nConfirm(plugin, "export-success")
                } else {
                    ctx.sender.sendI18nError(plugin, "export-fail")
                }
            }
        }
    }
}