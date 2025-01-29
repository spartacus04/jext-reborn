package me.spartacus04.jext.commands.executors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.spartacus04.jext.JextState.ASSETS_MANAGER
import me.spartacus04.jext.JextState.SCHEDULER
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.command.CommandSender

internal class ExecutorExport : ExecutorAdapter("jextexport", "export") {
    override fun execute(sender: CommandSender, args: Array<String>) {
        sender.sendJEXTMessage("export-start", hashMapOf(
            "file" to "exported.zip",
        ))

        SCHEDULER.runTaskAsynchronously {
            CoroutineScope(Dispatchers.Default).launch {
                if(ASSETS_MANAGER.tryExportResourcePack()) {
                    sender.sendJEXTMessage("export-success")
                } else {
                    sender.sendJEXTMessage("export-fail")
                }
            }
        }
    }
}