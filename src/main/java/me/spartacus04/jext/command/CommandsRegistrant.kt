package me.spartacus04.jext.command

import me.spartacus04.jext.config.ConfigData.Companion.VERSION
import org.bukkit.plugin.java.JavaPlugin

class CommandsRegistrant private constructor() {
    companion object {
        fun registerCommands(plugin: JavaPlugin) {
            ExecutorDisc().registerTo(plugin)
            ExecutorDiscGive().registerTo(plugin)
            ExecutorPlayMusic().registerTo(plugin)
            ExecutorPlayAt().registerTo(plugin)
            ExecutorStopMusic().registerTo(plugin)
            ExecutorReload(plugin).registerTo(plugin)
            ExecutorJukeboxGui(plugin).registerTo(plugin)

            if(VERSION >= "1.19") {
                ExecutorFragment().registerTo(plugin)
                ExecutorFragmentGive().registerTo(plugin)
            }
        }
    }
}