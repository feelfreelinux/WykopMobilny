package io.github.feelfreelinux.wykopmobilny.base

import android.content.pm.ActivityInfo
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
        printout("Error occured - " + e.message)
    }
}