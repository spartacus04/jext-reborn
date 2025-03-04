package me.spartacus04.jext.commands.executors

import me.spartacus04.jext.JextState.BASE_URL
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.commands.adapter.ParameterString
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.command.CommandSender

internal class ExecutorWebUi : ExecutorAdapter("jextwebui", "webui") {
    init {
        addParameter(ParameterString(false, listOf("config", "docs", "discs")))
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        if(!CONFIG.WEB_INTERFACE_API_ENABLED) {
            return sender.sendJEXTMessage("webui-disabled")
        }

        val page = if(args.isEmpty()) "" else when(args[0]) {
            "config" -> "config"
            "docs" -> "documentation"
            else -> ""
        }

        val ip = BASE_URL.getUrl(sender)

        sender.sendJEXTMessage("webui", hashMapOf(
            "url" to "https://spartacus04.github.io/jext-reborn/${page}?c=c&ip=${ip}&port=${CONFIG.WEB_INTERFACE_PORT}"
        ))
    }
}