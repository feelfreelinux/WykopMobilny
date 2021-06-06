package io.github.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.wykopmobilny.utils.preferences.SettingsPreferences

fun Context.createAlertBuilder(): AlertDialog.Builder {
    val useDarkTheme = SettingsPreferences(this).useDarkTheme
    return if (useDarkTheme) {
        AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog)
    } else {
        AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
    }
}
