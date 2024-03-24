package me.spartacus04.jext.commands.executors

import me.spartacus04.jext.commands.adapter.ExecutorAdapter
import me.spartacus04.jext.gui.AdminGui
import org.bukkit.entity.Player

class ExecutorAdminGui : ExecutorAdapter("jextadmingui", "admingui") {
    override fun executePlayer(sender: Player, args: Array<String>) {
        AdminGui(sender)
    }
}