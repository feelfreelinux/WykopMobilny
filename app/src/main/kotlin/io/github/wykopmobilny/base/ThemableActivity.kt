package io.github.wykopmobilny.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.wykopmobilny.R
import io.github.wykopmobilny.styles.AppThemeUi
import io.github.wykopmobilny.styles.StylesDependencies
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

internal abstract class ThemableActivity : AppCompatActivity() {

    private val getAppStyle by lazy { application.requireDependency<StylesDependencies>().getAppStyle() }

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme(runBlocking { getAppStyle().first().theme })
        super.onCreate(savedInstanceState ?: intent.getBundleExtra("saved_State"))

        lifecycleScope.launchWhenResumed {
            getAppStyle().distinctUntilChanged().drop(1).collect {
                updateTheme(it.theme)
                recreate()
            }
        }
    }

    private fun updateTheme(theme: AppThemeUi) {
        val themeRes = when (theme) {
            AppThemeUi.Light -> R.style.Theme_App_Light
            AppThemeUi.Dark -> R.style.Theme_App_Dark
            AppThemeUi.DarkAmoled -> R.style.Theme_App_Amoled
        }
        setTheme(themeRes)
    }
}
