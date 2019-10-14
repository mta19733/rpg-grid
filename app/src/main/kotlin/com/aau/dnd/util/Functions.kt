package com.aau.dnd.util

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast
        .makeText(context, text, duration)
        .show()
}
