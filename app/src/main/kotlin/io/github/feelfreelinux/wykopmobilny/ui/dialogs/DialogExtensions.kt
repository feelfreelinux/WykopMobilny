package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Build

@Suppress("DEPRECATION")
fun Context.createAlertBuilder() : AlertDialog.Builder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
        else AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)