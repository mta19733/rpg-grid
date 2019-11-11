package com.aau.rpg.presentation.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aau.rpg.R

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
 * Display an [AlertDialog] on [Fragment].
 */
fun Fragment.showAlertDialog(
    titleText: String,
    positiveButtonText: String,
    onSubmit: () -> Unit,
    negativeButtonText: String = getString(R.string.button_cancel),
    messageText: String? = null,
    view: View? = null
): AlertDialog {
    val dialog = AlertDialog
        .Builder(requireContext())
        .setTitle(titleText)
        .setPositiveButton(positiveButtonText) { dialog, _ ->
            onSubmit()
            dialog.cancel()
        }
        .setNegativeButton(negativeButtonText) { dialog, _ ->
            dialog.cancel()
        }

    if (messageText != null) {
        dialog.setMessage(messageText)
    }

    if (view != null) {
        dialog.setView(view)
    }

    return dialog.show()
}

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
