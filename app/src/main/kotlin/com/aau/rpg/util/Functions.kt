package com.aau.rpg.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Display a toast notification on [Activity].
 */
fun Activity.toast(text: String, duration: Int = Toast.LENGTH_LONG) =
    toast(baseContext, text, duration)

/**
 * Display a toast notification on [Fragment].
 */
fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_LONG) =
    toast(context, text, duration)

private fun toast(context: Context?, text: String, duration: Int): Unit = Toast
    .makeText(context, text, duration)
    .show()
