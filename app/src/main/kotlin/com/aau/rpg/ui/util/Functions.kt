package com.aau.rpg.ui.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Display a toast notification on [Activity].
 */
fun Activity.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    toast(baseContext, text, duration)

/**
 * Display a toast notification on [Fragment].
 */
fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    toast(context, text, duration)

/**
 * Log a debug log message using [this] as tag.
 */
fun Any.logDebug(message: String) {
    Log.d(this::class.java.name, message)
}

/**
 * Log a error log message using [this] as tag.
 */
fun Any.logError(message: String, error: Throwable? = null) {
    Log.e(this::class.java.name, message, error)
}

private fun toast(context: Context?, text: String, duration: Int): Unit = Toast
    .makeText(context, text, duration)
    .apply {
        setGravity(Gravity.CENTER, 0, 0)
    }
    .show()
