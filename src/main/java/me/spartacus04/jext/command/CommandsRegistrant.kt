package me.spartacus04.jext.command

import org.bukkit.plugin.java.JavaPlugin


/**
 * The `CommandsRegistrant` class is a utility class used to register commands.
 *
 * @constructor Class is singleton, so the constructor is private.
 */
internal class CommandsRegistrant private constructor() {
    companion object {
        /**
         * The function `registerCommands` registers all commands to the plugin.
         *
         * @param plugin The plugin instance that you want to register the commands to.
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