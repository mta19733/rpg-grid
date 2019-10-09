package com.aau.dnd.util

import android.app.Application
import androidx.fragment.app.Fragment
import com.aau.dnd.core.DndGridContext

val Fragment.ctx
    get(): DndGridContext = activity
        ?.application
        ?.let<Application, DndGridContext?> { application ->
            if (application is DndGridContext) {
                application
            } else {
                null
            }
        }
        ?: throw IllegalStateException("Application context could not be retrieved in fragment")
