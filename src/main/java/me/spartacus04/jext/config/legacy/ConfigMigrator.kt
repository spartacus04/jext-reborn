package me.spartacus04.jext.config.legacy

import me.spartacus04.colosseum.ColosseumPlugin

internal interface ConfigMigrator {
    fun migrateToNext(plugin: ColosseumPlugin) : String
}
