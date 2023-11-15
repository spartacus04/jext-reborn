package me.spartacus04.jext.utils

import me.spartacus04.jext.State.CONFIG
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.config.fields.FieldJukeboxBehaviour
import me.spartacus04.jext.utils.Constants.BSTATS_METRICS
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie

internal class JextMetrics : Metrics(PLUGIN, BSTATS_METRICS) {
    init {
        super.addCustomChart(SimplePie("juke_gui") {
            when(CONFIG.JUKEBOX_BEHAVIOUR) {
                FieldJukeboxBehaviour.VANILLA -> return@SimplePie "Vanilla"
                else -> return@SimplePie "GUI"
            }
        })
    }

    companion object {
        var METRICS = if(CONFIG.ALLOW_METRICS) {
            JextMetrics()
        } else {
            null
        }

        fun reloadMetrics() {
            METRICS?.shutdown()
            METRICS = null

            METRICS = if(CONFIG.ALLOW_METRICS) {
                JextMetrics()
            } else {
                null
            }
        }
    }
}