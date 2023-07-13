package me.spartacus04.jext.command

import org.bukkit.plugin.java.JavaPlugin

class CommandsRegistrant private constructor() {
    companion object {
        /**
         * The function registers various commands to a JavaPlugin in Kotlin.
         *
         * @param plugin The "plugin" parameter is an instance of the JavaPlugin class. It is used to register the commands
         * to the plugin, allowing them to be executed within the plugin's context.
         */
        fun registerCommands(plugin: JavaPlugin) {
            ExecutorDisc().registerTo(plugin)
            ExecutorDiscGive().registerTo(plugin)
            ExecutorPlayMusic().registerTo(plugin)
            ExecutorPlayAt().registerTo(plugin)
            ExecutorStopMusic().registerTo(plugin)
            ExecutorReload(plugin).registerTo(plugin)
            ExecutorJukeboxGui().registerTo(plugin)
            ExecutorFragment().registerTo(plugin)
            ExecutorFragmentGive().registerTo(plugin)

            ExecutorLegacyJukeboxGui(plugin).registerTo(plugin)
        }
    }
}