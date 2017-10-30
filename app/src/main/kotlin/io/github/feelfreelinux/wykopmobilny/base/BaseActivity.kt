package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi

// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : AppCompatActivity() {
    fun showErrorDialog(e : Throwable) =
        showExceptionDialog(e)

    private val themeSettingsPreferences by lazy { SettingsPreferences(this) as SettingsPreferencesApi }

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
    }

    private fun initTheme() {
        if (themeSettingsPreferences.useDarkTheme) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme)
        }
    }
}