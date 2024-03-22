package me.spartacus04.jext.commands

import me.spartacus04.jext.commands.executors.*
import me.spartacus04.jext.commands.executors.ExecutorFragment
import me.spartacus04.jext.commands.executors.ExecutorFragmentGive
import me.spartacus04.jext.commands.executors.ExecutorJukeboxGui
import me.spartacus04.jext.commands.executors.ExecutorReload

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
        ExecutorAdminGui()
    )
    fun registerCommands() {
        commandRegistry.forEach {
            it.register()
        }

        ExecutorMain().register()
    }
}