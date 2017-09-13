package io.github.feelfreelinux.wykopmobilny.base

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.utils.printout

// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : AppCompatActivity() {
    val kodein = LazyKodein(appKodein)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun showErrorDialog(e : Exception) =
        showExceptionDialog(e)
}