package io.github.wykopmobilny.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.github.wykopmobilny.R
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.dialogs.showExceptionDialog
import javax.inject.Inject

/**
 * This class should be extended in all activities in this app. Place global-activity settings here.
 */
abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    open val enableSwipeBackLayout: Boolean = false
    open val isActivityTransfluent: Boolean = false
    var isRunning = false
    lateinit var rxPermissions: RxPermissions

    @Inject
    lateinit var themeSettingsPreferences: SettingsPreferencesApi

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        initTheme()
        super.onCreate(savedInstanceState)
        rxPermissions = RxPermissions(this)
        if (enableSwipeBackLayout) {
            Slidr.attach(
                this,
                SlidrConfig.Builder().edge(true).build()
            )
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
        if (themeSettingsPreferences.useDarkTheme) {
            if (themeSettingsPreferences.useAmoledTheme) {
                setTheme(R.style.WykopAppTheme_Amoled)
                window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark_Amoled)
            } else {
                setTheme(R.style.WykopAppTheme_Dark)
                window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark_Dark)
            }
        } else {
            setTheme(R.style.WykopAppTheme)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        if (isActivityTransfluent || enableSwipeBackLayout) {
            theme.applyStyle(R.style.TransparentActivityTheme, true)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        when (themeSettingsPreferences.fontSize) {
            "tiny" -> theme.applyStyle(R.style.TextSizeTiny, true)
            "small" -> theme.applyStyle(R.style.TextSizeSmall, true)
            "normal" -> theme.applyStyle(R.style.TextSizeNormal, true)
            "large" -> theme.applyStyle(R.style.TextSizeLarge, true)
            "huge" -> theme.applyStyle(R.style.TextSizeHuge, true)
        }
    }

    fun showErrorDialog(e: Throwable) {
        if (isRunning) showExceptionDialog(e)
    }

    override fun androidInjector() = androidInjector
}
