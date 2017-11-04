package io.github.feelfreelinux.wykopmobilny.ui.modules.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
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
    lateinit var navigatorApi: NavigatorApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ustawienia"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
                .replace(R.id.settings_fragment, SettingsFragment())
                .commit()
    }


    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.app_preferences)
            (findPreference("notificationsSchedulerDelay") as ListPreference).apply {
                summary = entry
            }

            (findPreference("hotEntriesScreen") as ListPreference).apply {
                summary = entry
            }
        }

        override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences, key: String) {
            val pref = findPreference(key)

            if (pref is ListPreference) {
                pref.setSummary(pref.entry)
            } else if (pref is CheckBoxPreference) {
                when (pref.key) {
                    "useDarkTheme" -> {
                        restartActivity()
                    }
                }
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        private fun restartActivity() {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(THEME_CHANGED_EXTRA, true)
            startActivity(intent)
            activity.finish()
        }

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
            navigatorApi.openMainActivity(this)
        }
        finish()
    }
}