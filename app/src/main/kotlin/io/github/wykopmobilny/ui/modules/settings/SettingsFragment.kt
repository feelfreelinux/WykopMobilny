package io.github.wykopmobilny.ui.modules.settings

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.takisoft.preferencex.PreferenceFragmentCompat
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.blacklist.BlacklistActivity
import io.github.wykopmobilny.ui.modules.notifications.notificationsservice.NotificationPiggyback
import io.github.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.wykopmobilny.ui.modules.search.SuggestionDatabase
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, HasAndroidInjector {

    @Inject
    lateinit var settingsApi: SettingsPreferencesApi

    @Inject
    lateinit var userManagerApi: UserManagerApi

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = androidInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        (findPreference("piggyBackPushNotifications") as CheckBoxPreference?)?.isEnabled = true
        (findPreference("notificationsSchedulerDelay") as ListPreference?)?.apply {
            summary = entry
        }

        (findPreference("showNotifications") as CheckBoxPreference?)?.isEnabled =
            !(findPreference("piggyBackPushNotifications") as CheckBoxPreference?)!!.isChecked
        (findPreference("appearance") as Preference?)?.setOnPreferenceClickListener {
            (activity as SettingsActivity).openFragment(SettingsAppearance(), "appearance")
            true
        }

        (findPreference("blacklist") as Preference?)!!.setOnPreferenceClickListener {
            userManagerApi.runIfLoggedIn(requireActivity()) {
                startActivity(BlacklistActivity.createIntent(requireActivity()))
            }
            true
        }

        (findPreference("clearhistory") as Preference?)?.setOnPreferenceClickListener {
            SuggestionDatabase(requireContext()).clearDb()
            Toast.makeText(requireContext(), "Wyczyszczono historię wyszukiwarki", Toast.LENGTH_LONG).show()
            true
        }
    }

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences, key: String) {
        val pref = findPreference<Preference>(key)
        if (pref is CheckBoxPreference) {
            when (pref.key) {
                "showNotifications" -> {
                    findPreference<Preference>("notificationsSchedulerDelay")?.isEnabled = pref.isChecked
                    findPreference<Preference>("piggyBackPushNotifications")?.isEnabled = pref.isChecked
                    if (pref.isChecked) {
                        WykopNotificationsJob.schedule(settingsApi)
                    } else {
                        WykopNotificationsJob.cancel()
                    }
                }

                "piggyBackPushNotifications" -> {
                    findPreference<Preference>("notificationsSchedulerDelay")?.isEnabled = !pref.isChecked
                    (findPreference("showNotifications") as Preference?)?.isEnabled = !pref.isChecked
                    if (pref.isChecked) {
                        if (isOfficialAppInstalled()) {
                            if (isNotificationAccessEnabled()) {
                                WykopNotificationsJob.cancel()
                            } else {
                                pref.isChecked = false
                                onSharedPreferenceChanged(sharedPrefs, key)
                                Toast.makeText(requireActivity(), R.string.toast_allow_notification_access, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                            }
                        } else {
                            pref.isChecked = false
                            onSharedPreferenceChanged(sharedPrefs, key)
                            openWykopMarketPage()
                        }
                    } else {
                        if ((findPreference("showNotifications") as CheckBoxPreference?)!!.isChecked) {
                            WykopNotificationsJob.schedule(settingsApi)
                        } else {
                            WykopNotificationsJob.cancel()
                        }
                    }
                }
            }
        } else if (pref is ListPreference) {
            when (pref.key) {
                "notificationsSchedulerDelay" -> {
                    (findPreference("notificationsSchedulerDelay") as ListPreference?)?.apply {
                        summary = entry
                        WykopNotificationsJob.schedule(settingsApi)
                    }
                }
            }
        }
    }

    private fun isOfficialAppInstalled(): Boolean {
        return try {
            requireActivity().packageManager.getApplicationInfo("pl.wykop.droid", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun isNotificationAccessEnabled(): Boolean {
        val manager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { NotificationPiggyback::class.java.name == it.service.className }
    }

    private fun openWykopMarketPage() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle(R.string.dialog_piggyback_market_title)
            setMessage(R.string.dialog_piggyback_market_message)
            setCancelable(false)
            setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=pl.wykop.droid")
                startActivity(intent)
            }
            create()
            show()
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
}
