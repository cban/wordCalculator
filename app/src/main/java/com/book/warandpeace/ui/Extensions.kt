package com.book.warandpeace.ui

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import java.text.Normalizer


fun Activity.showToast(
        message: String,
    ) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun View.visible(): View {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

    fun View.hide(): View {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }

fun MaterialButton.setStateEnabled() {
    this.isEnabled = true
}

fun MaterialButton.setStateDisabled() {
    this.isEnabled = false
}


fun String.normalizer(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")
        .lowercase()
        .replace(Regex("[^a-z\\s]+"), " ")
}
