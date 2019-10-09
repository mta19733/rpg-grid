package com.aau.dnd.util

import android.app.Activity
import com.aau.dnd.core.DndGridContext

val Activity.ctx: DndGridContext
    get(): DndGridContext = if (application is DndGridContext) {
        application as DndGridContext
    } else {
        throw IllegalStateException("Application context could not be retrieved")
    }
