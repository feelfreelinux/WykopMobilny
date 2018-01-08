package io.github.feelfreelinux.wykopmobilny.ui.modules.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class SettingsActivity : BaseActivity() {
    companion object {
        val THEME_CHANGED_EXTRA = "THEME_CHANGED"
        val THEME_CHANGED_RESULT = 154

        fun createIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    @Inject
    lateinit var navigatorApi: NewNavigatorApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ustawienia"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
                .replace(R.id.settings_fragment, SettingsFragment())
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onBackPressed() {
        if (intent.hasExtra(THEME_CHANGED_EXTRA)) {
            setResult(THEME_CHANGED_RESULT)
            navigatorApi.openMainActivity()
        }
        finish()
    }
}