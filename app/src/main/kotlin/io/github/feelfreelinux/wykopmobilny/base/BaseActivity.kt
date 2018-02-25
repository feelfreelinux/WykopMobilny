package io.github.feelfreelinux.wykopmobilny.base

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.app.SwipeBackActivityBase
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.app.SwipeBackActivityHelper
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.SwipeBackLayout
import io.github.feelfreelinux.wykopmobilny.ui.swipeback.SwipeLayoutUtils


// This class should be extended in all activities in this app. Place global-activity settings here
abstract class BaseActivity : DaggerAppCompatActivity(), SwipeBackActivityBase {
    lateinit var swipeBackHandler : SwipeBackActivityHelper
    open val enableSwipeBackLayout : Boolean = false
    fun showErrorDialog(e : Throwable) {
        showExceptionDialog(e)
    }

    private val themeSettingsPreferences by lazy { SettingsPreferences(this) as SettingsPreferencesApi }

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
        if (enableSwipeBackLayout) {
            swipeBackHandler = SwipeBackActivityHelper(this)
            swipeBackHandler.onActivityCreate()
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (enableSwipeBackLayout) swipeBackHandler.onPostCreate()
    }

    override fun <T : View?> findViewById(id: Int): T {
        val v = super.findViewById<T>(id)
        if (v == null && ::swipeBackHandler.isInitialized) return swipeBackHandler.findViewById(id) as T
        return v
    }

    override fun getSwipeBackLayout(): SwipeBackLayout? {
        return if (enableSwipeBackLayout) {
            swipeBackHandler.swipeBackLayout!!
        } else {
            null
        }
    }

    override fun setSwipeBackEnable(isEnabled: Boolean) {
        if (enableSwipeBackLayout) getSwipeBackLayout()!!.setEnableGesture(isEnabled)
    }

    override fun scrollToFinishActivity() {
        if (enableSwipeBackLayout) {
            SwipeLayoutUtils.convertActivityToTranslucent(this)
            getSwipeBackLayout()!!.scrollToFinishActivity()
        }
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