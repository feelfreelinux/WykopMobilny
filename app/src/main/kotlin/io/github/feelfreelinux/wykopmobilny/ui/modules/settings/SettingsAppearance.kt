package io.github.feelfreelinux.wykopmobilny.ui.modules.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import javax.inject.Inject


class SettingsAppearance : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, HasSupportFragmentInjector {

    @Inject lateinit var settingsApi: SettingsPreferencesApi
    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun supportFragmentInjector() = childFragmentInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences_appearance)
        findPreference("useAmoledTheme").isEnabled = settingsApi.useDarkTheme
        findPreference("cutImageProportion").isEnabled = settingsApi.cutImages
        findPreference("linkImagePosition").isEnabled = !settingsApi.linkSimpleList && settingsApi.linkShowImage
        findPreference("linkShowAuthor").isEnabled = !settingsApi.linkSimpleList

        (findPreference("hotEntriesScreen") as ListPreference).apply {
            summary = entry
        }

        (findPreference("fontSize") as ListPreference).apply {
            summary = entry
        }

        (findPreference("defaultScreen") as ListPreference).apply {
            summary = entry
        }

        (findPreference("linkImagePosition") as ListPreference).apply {
            summary = entry
        }
    }

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences, key: String) {
        val pref = findPreference(key)
        findPreference("useAmoledTheme").isEnabled = settingsApi.useDarkTheme
        findPreference("cutImageProportion").isEnabled = settingsApi.cutImages
        findPreference("linkShowAuthor").isEnabled = !settingsApi.linkSimpleList
        findPreference("linkImagePosition").isEnabled = !settingsApi.linkSimpleList

        if (pref is ListPreference) {
            pref.setSummary(pref.entry)
            when (pref.key) {
                "linkImagePosition", "fontSize" -> (activity as SettingsActivity).shouldRestartMainScreen = true
            }
        } else if (pref is CheckBoxPreference) {
            when (pref.key) {
                "linkShowAuthor", "linkSimpleList", "linkShowImage" -> (activity as SettingsActivity).shouldRestartMainScreen = true
                "useDarkTheme", "useAmoledTheme" -> restartActivity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    private fun restartActivity() {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.putExtra(SettingsActivity.THEME_CHANGED_EXTRA, true)
        intent.putExtra(SettingsActivity.EXTRA_SCREEN, SettingsActivity.SCREEN_APPEARANCE)
        startActivity(intent)
        activity?.finish()
    }
}