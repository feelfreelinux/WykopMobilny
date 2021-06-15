package io.github.wykopmobilny.ui.modules.loginscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.ActivityContainerBinding
import io.github.wykopmobilny.styles.AppThemeUi
import io.github.wykopmobilny.styles.StylesDependencies
import io.github.wykopmobilny.ui.login.android.loginFragment
import io.github.wykopmobilny.utils.requireDependency
import io.github.wykopmobilny.utils.viewBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class LoginScreenActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityContainerBinding::inflate)

    private val getAppStyle by lazy { application.requireDependency<StylesDependencies>().getAppStyle() }

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme(runBlocking { getAppStyle().first().theme })
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, loginFragment())
                .commit()
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

    companion object {

        fun createIntent(context: Context) = Intent(context, LoginScreenActivity::class.java)
    }
}
