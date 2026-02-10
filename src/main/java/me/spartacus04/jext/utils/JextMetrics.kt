package me.spartacus04.jext.utils

import me.spartacus04.jext.Jext.Companion.INSTANCE
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.utils.Constants.BSTATS_METRICS
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie

internal class JextMetrics : Metrics(INSTANCE, BSTATS_METRICS) {
    init {
        super.addCustomChart(SimplePie("juke_gui") {
            when(INSTANCE.config.JUKEBOX_BEHAVIOUR) {
                FieldJukeboxBehaviour.VANILLA -> return@SimplePie "Vanilla"
                else -> return@SimplePie "GUI"
            }
        })
    }

    companion object {
        private var METRICS = if(INSTANCE.config.ALLOW_METRICS) {
            JextMetrics()
        } else {
            null
        }

        fun reloadMetrics() {
            METRICS?.shutdown()
            METRICS = null

            METRICS = if(INSTANCE.config.ALLOW_METRICS) {
                JextMetrics()
            } else {
                null
            }
        }
    }
}