package io.github.feelfreelinux.wykopmobilny.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import dagger.android.support.DaggerAppCompatActivity
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.app.SwipeBackActivityHelper
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi


// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : DaggerAppCompatActivity() {
    lateinit var swipeBackHandler : SwipeBackActivityHelper
    open val enableSwipeBackLayout : Boolean = false
    open val isActivityTransfluent : Boolean = false
    public var isRunning = false
    fun showErrorDialog(e : Throwable) {
        if (isRunning) {
            showExceptionDialog(e)
        }
    }

    private val themeSettingsPreferences by lazy { SettingsPreferences(this) as SettingsPreferencesApi }

    override fun onResume() {
        isRunning = true
        super.onResume()
    }

    override fun onPause() {
        isRunning = false
        super.onPause()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
        if (enableSwipeBackLayout) {
            Slidr.attach(this,
                    SlidrConfig.Builder().edge(true).build())
        }

    }


    /**
     * This function initializes activity theme based on settings
     */
    private fun initTheme() {
        if (themeSettingsPreferences.useDarkTheme) {
            if (themeSettingsPreferences.useAmoledTheme) {
                setTheme(R.style.WykopAppTheme_Amoled)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark_Amoled)
            } else {
                setTheme(R.style.WykopAppTheme_Dark)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark_Dark)
            }
        } else {
            setTheme(R.style.WykopAppTheme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
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
}