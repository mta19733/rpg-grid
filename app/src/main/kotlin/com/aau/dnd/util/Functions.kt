package com.aau.dnd.util

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast
        .makeText(baseContext, text, duration)
        .show()
}

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast
        .makeText(context, text, duration)
        .show()
}
