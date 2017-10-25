package io.github.feelfreelinux.wykopmobilny.base

import android.support.v7.app.AppCompatActivity
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog

// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : AppCompatActivity() {
    fun showErrorDialog(e : Throwable) =
        showExceptionDialog(e)
}