package io.github.feelfreelinux.wykopmobilny.ui.modules.settings

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.takisoft.preferencex.PreferenceFragmentCompat
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist.BlacklistActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.NotificationPiggyback
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJob
import io.github.feelfreelinux.wykopmobilny.ui.modules.search.SuggestionDatabase
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, HasSupportFragmentInjector {

    @Inject lateinit var settingsApi: SettingsPreferencesApi
    @Inject lateinit var userManagerApi: UserManagerApi
    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun supportFragmentInjector() = childFragmentInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
        (findPreference("piggyBackPushNotifications") as CheckBoxPreference).isEnabled =
                (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
        (findPreference("notificationsSchedulerDelay") as ListPreference).apply {
            summary = entry
        }

        (findPreference("showNotifications") as CheckBoxPreference).isEnabled =
                !(findPreference("piggyBackPushNotifications") as CheckBoxPreference).isChecked
        (findPreference("appearance") as Preference).setOnPreferenceClickListener {
            (activity as SettingsActivity).openFragment(SettingsAppearance(), "appearance")
            true
        }

        (findPreference("blacklist") as Preference).setOnPreferenceClickListener {
            userManagerApi.runIfLoggedIn(activity!!) {
                startActivity(BlacklistActivity.createIntent(activity!!))
            }
            true
        }

        (findPreference("clearhistory") as Preference).setOnPreferenceClickListener {
            SuggestionDatabase(context!!).clearDb()
            Toast.makeText(context!!, "Wyczyszczono historię wyszukiwarki", Toast.LENGTH_LONG).show()
            true
        }
    }

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences, key: String) {
        val pref = findPreference(key)
        if (pref is CheckBoxPreference) {
            when (pref.key) {
                "showNotifications" -> {
                    findPreference("notificationsSchedulerDelay").isEnabled = pref.isChecked
                    findPreference("piggyBackPushNotifications").isEnabled = pref.isChecked
                    if (pref.isChecked) {
                        WykopNotificationsJob.schedule(settingsApi)
                    } else {
                        WykopNotificationsJob.cancel()
                    }
                }

                "piggyBackPushNotifications" -> {
                    findPreference("notificationsSchedulerDelay").isEnabled = !pref.isChecked
                    (findPreference("showNotifications") as Preference).isEnabled = !pref.isChecked
                    if (pref.isChecked) {
                        if (isOfficialAppInstalled()) {
                            if (isNotificationAccessEnabled()) {
                                WykopNotificationsJob.cancel()
                            } else {
                                pref.isChecked = false
                                onSharedPreferenceChanged(sharedPrefs, key)
                                Toast.makeText(activity!!, R.string.toast_allow_notification_access, Toast.LENGTH_SHORT).show()
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                } else {
                                    startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                                }
                            }
                        } else {
                            pref.isChecked = false
                            onSharedPreferenceChanged(sharedPrefs, key)
                            openWykopMarketPage()
                        }
                    } else {
                        if ((findPreference("showNotifications") as CheckBoxPreference).isChecked) {
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
                    (findPreference("notificationsSchedulerDelay") as ListPreference).apply {
                        summary = entry
                        WykopNotificationsJob.schedule(settingsApi)
                    }
                }
            }
        }
    }

    private fun isOfficialAppInstalled(): Boolean {
        return try {
            activity!!.packageManager.getApplicationInfo("pl.wykop.droid", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun isNotificationAccessEnabled(): Boolean {
        val manager = activity!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        return manager.getRunningServices(
            Integer.MAX_VALUE
        ).any { NotificationPiggyback::class.java.name == it.service.className }
    }

    private fun openWykopMarketPage() {
        activity!!.createAlertBuilder().apply {
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