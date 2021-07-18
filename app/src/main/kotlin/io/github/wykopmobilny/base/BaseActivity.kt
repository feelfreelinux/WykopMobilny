package io.github.wykopmobilny.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.github.wykopmobilny.R
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.styles.AppThemeUi
import io.github.wykopmobilny.styles.StyleUi
import io.github.wykopmobilny.styles.StylesDependencies
import io.github.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * This class should be extended in all activities in this app. Place global-activity settings here.
 */
abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    open val enableSwipeBackLayout: Boolean = false
    open val isActivityTransfluent: Boolean = false
    var isRunning = false

    @Inject
    lateinit var themeSettingsPreferences: SettingsPreferencesApi

    private val getAppStyle by lazy { application.requireDependency<StylesDependencies>().getAppStyle() }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        initTheme()
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            getAppStyle().distinctUntilChanged().drop(1).collect {
                updateTheme(it)
                recreate()
            }
        }
    }

    override fun onResume() {
        isRunning = true
        super.onResume()
    }

    override fun onPause() {
        isRunning = false
        super.onPause()
    }

    // This function initializes activity theme based on settings
    private fun initTheme() {
        updateTheme(runBlocking { getAppStyle().first() })
        if (isActivityTransfluent || enableSwipeBackLayout) {
            theme.applyStyle(R.style.TransparentActivityTheme, true)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        when (themeSettingsPreferences.fontSize) {
            "tiny" -> theme.applyStyle(R.style.TextSizeTiny, true)
            "small" -> theme.applyStyle(R.style.TextSizeSmall, true)
            "large" -> theme.applyStyle(R.style.TextSizeLarge, true)
            "huge" -> theme.applyStyle(R.style.TextSizeHuge, true)
            "normal",
            null,
            -> theme.applyStyle(R.style.TextSizeNormal, true)
        }
    }

    private fun updateTheme(appStyle: StyleUi) {
        val (appTheme, navColor) = runBlocking {
            when (appStyle.theme) {
                AppThemeUi.Light -> R.style.WykopAppTheme to R.color.colorPrimaryDark
                AppThemeUi.Dark -> R.style.WykopAppTheme_Dark to R.color.colorPrimaryDark_Dark
                AppThemeUi.DarkAmoled -> R.style.WykopAppTheme_Amoled to R.color.colorPrimaryDark_Amoled
            }
        }
        setTheme(appTheme)
        window.navigationBarColor = ContextCompat.getColor(this@BaseActivity, navColor)
    }

    fun showErrorDialog(e: Throwable) {
        if (isRunning) showExceptionDialog(e)
    }

    override fun androidInjector() = androidInjector
}
