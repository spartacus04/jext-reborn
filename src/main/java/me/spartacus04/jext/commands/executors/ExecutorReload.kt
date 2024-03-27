package me.spartacus04.jext.commands.executors

import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.DISCS
import me.spartacus04.jext.JextState.INTEGRATIONS
import me.spartacus04.jext.JextState.WEBSERVER
import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.utils.JextMetrics
import me.spartacus04.jext.utils.sendJEXTMessage
import org.bukkit.command.CommandSender

/**
 * ExecutorReload is a class used to register the "jext" command to the plugin.
 *
 */
internal class ExecutorReload : ExecutorAdapter("jextreload", "reload") {
    /**
     * The function `execute` reloads the plugin.
     *
     * @param sender The sender who executed the command.
     * @param args The arguments that were passed to the command.
     */
    override fun execute(sender: CommandSender, args: Array<String>) {
        CONFIG.read()
        DISCS.reloadDiscs()
        INTEGRATIONS.reloadDefaultIntegrations()
        JextMetrics.reloadMetrics()
        WEBSERVER.reload()

        sender.sendJEXTMessage("reloaded")
    }
}