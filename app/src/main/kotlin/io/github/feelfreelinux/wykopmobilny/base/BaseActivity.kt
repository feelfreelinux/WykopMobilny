package io.github.feelfreelinux.wykopmobilny.base

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import io.github.feelfreelinux.wykopmobilny.utils.printout

// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun showErrorDialog(e : Throwable) {
        val message = if (e.message.isNullOrEmpty()) e.toString() else e.message
        printout("Error occured - " + message)
        val alertBuilder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
        else AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        alertBuilder.run {
            setTitle("Wystąpił nieoczekiwany problem")
            setMessage(message)
            create().show()
        }
    }
}