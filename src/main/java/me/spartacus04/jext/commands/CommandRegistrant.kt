package me.spartacus04.jext.commands

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
        ExecutorWebUi()
    )
    fun registerCommands() {
        commandRegistry.forEach {
            it.register()
        }

        MainCommand().register()
    }
}