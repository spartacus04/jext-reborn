package me.spartacus04.jext.config.legacy

internal interface ConfigMigrator {
    fun migrateToNext() : String
}
