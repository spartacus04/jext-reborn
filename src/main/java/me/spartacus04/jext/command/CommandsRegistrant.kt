package me.spartacus04.jext.command

import org.bukkit.plugin.java.JavaPlugin

class CommandsRegistrant private constructor() {
    companion object {
        fun registerCommands(plugin: JavaPlugin) {
            ExecutorDisc().registerTo(plugin)
            ExecutorDiscGive().registerTo(plugin)
            ExecutorPlayMusic().registerTo(plugin)
            ExecutorPlayAt().registerTo(plugin)
            ExecutorStopMusic().registerTo(plugin)
        }
    }
}