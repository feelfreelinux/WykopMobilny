package io.github.feelfreelinux.wykopmobilny.base

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import dagger.android.support.DaggerAppCompatActivity
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi

// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : DaggerAppCompatActivity() {
    fun showErrorDialog(e : Throwable) {
        showExceptionDialog(e)
    }

    private val themeSettingsPreferences by lazy { SettingsPreferences(this) as SettingsPreferencesApi }

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
    }

    private fun initTheme() {
        if (themeSettingsPreferences.useDarkTheme) {
            if (themeSettingsPreferences.useAmoledTheme) {
                setTheme(R.style.AppTheme_Amoled)
            } else {
                setTheme(R.style.AppTheme_Dark)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark_Dark))
            }
        } else {
            setTheme(R.style.AppTheme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        }
    }
}