package me.spartacus04.jext.commands

internal object CommandRegistrant {
    fun registerCommands() {
        ExecutorDisc().register()
        ExecutorDiscGive().register()
        ExecutorFragment().register()
        ExecutorFragmentGive().register()
        ExecutorJukeboxGui().register()
        ExecutorPlayAt().register()
        ExecutorPlayMusic().register()
        ExecutorReload().register()
        ExecutorStopMusic().register()
    }
}