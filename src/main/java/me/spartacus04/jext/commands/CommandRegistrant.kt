package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.executors.*

internal object CommandRegistrant {
    val commandRegistry = listOf(
        ExecutorDisc(),
        ExecutorDiscGive(),
        ExecutorFragment(),
        ExecutorFragmentGive(),
        ExecutorJukeboxGui(),
        ExecutorPlayAt(),
        ExecutorPlayMusic(),
        ExecutorReload(),
        ExecutorStopMusic(),
        ExecutorWebUi(),
        ExecutorAdminGui(),
        ExecutorExport()
    )
    fun registerCommands() {
        commandRegistry.forEach {
            it.register()
        }

        ExecutorMain().register()
    }
}