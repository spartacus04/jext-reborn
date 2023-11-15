package me.spartacus04.jext.utils

import me.spartacus04.jext.State.VERSION
import me.spartacus04.jext.utils.Constants.FRAGMENT_LIST
import org.bukkit.Material

/**
 * Checks if the material is a record fragment
 */
val Material.isRecordFragment: Boolean
    get() {
        return FRAGMENT_LIST.contains(this)
    }