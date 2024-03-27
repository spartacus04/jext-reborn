package me.spartacus04.jext.commands.executors

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.net.InetAddress

internal class ExecutorWebUi : ExecutorAdapter("jextwebui", "webui") {
    override fun execute(sender: CommandSender, args: Array<String>) {
        if(!CONFIG.WEB_INTERFACE_API_ENABLED) {
            return sender.sendJEXTMessage("webui-disabled")
        }

        val page = if(args.isEmpty()) "" else when(args[0]) {
            "config" -> "config"
            "docs" -> "documentation"
            else -> ""
        }

        val url = if(sender is ConsoleCommandSender || (sender as Player).address == InetAddress.getLocalHost())
            "https://spartacus04.github.io/jext-reborn/${page}?c=c&ip=127.0.0.1&port=${CONFIG.WEB_INTERFACE_PORT}"
        else
            "https://spartacus04.github.io/jext-reborn/${page}?c=c&port=${CONFIG.WEB_INTERFACE_PORT}"

        sender.sendJEXTMessage("webui", hashMapOf(
            "url" to url
        ))
    }
}