package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences

@Suppress("DEPRECATION")
fun Context.createAlertBuilder() : AlertDialog.Builder {
    val useDarkTheme = SettingsPreferences(this).useDarkTheme
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        if (useDarkTheme) {
            AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog)
        } else AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog)

    } else {
        if (useDarkTheme) {
            AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        } else {
            AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        }
    }
}