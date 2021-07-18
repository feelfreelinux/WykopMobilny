package io.github.wykopmobilny.ui.settings.android

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import io.github.wykopmobilny.ui.settings.GeneralPreferencesUi.NotificationsUi.RefreshPeriodUi
import io.github.wykopmobilny.ui.settings.GetGeneralPreferences
import io.github.wykopmobilny.ui.settings.android.di.DaggerSettingsUiComponent
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

internal class GeneralPreferencesFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var getGeneralPreferences: GetGeneralPreferences

    override fun onAttach(context: Context) {
        DaggerSettingsUiComponent.factory()
            .create(
                deps = context.requireDependency(),
            )
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.general_preferences, rootKey)

        lifecycleScope.launchWhenResumed {
            getGeneralPreferences().collect {
                bindPreference("appearance", ::openAppearanceSettings)
                bindCheckbox("showNotifications", it.notifications.notificationsEnabled)
                bindCheckbox("disableExitConfirmation", it.notifications.exitConfirmation)
                bindCheckbox("showAdultContent", it.filtering.showPlus18Content)
                bindCheckbox("hideNsfw", it.filtering.showNsfwContent)
                bindCheckbox("hideLowRangeAuthors", it.filtering.hideNewUserContent)
                bindCheckbox("hideContentWithoutTags", it.filtering.hideContentWithNoTags)
                bindCheckbox("hideBlacklistedViews", it.filtering.hideBlacklistedContent)
                bindPreference("blacklist", it.filtering.manageBlackList)
                bindCheckbox("useBuiltInBrowser", it.filtering.useEmbeddedBrowser)
                bindPreference("clearhistory", it.filtering.clearSearchHistory)
                bindList(
                    key = "notificationsSchedulerDelay",
                    setting = it.notifications.notificationRefreshPeriod,
                    mapping = refreshPeriodMapping,
                )
            }
        }
    }

    private val refreshPeriodMapping by lazy {
        RefreshPeriodUi.values().associateWith { period ->
            when (period) {
                RefreshPeriodUi.FifteenMinutes -> R.string.preferences_notification_period_15_minutes
                RefreshPeriodUi.ThirtyMinutes -> R.string.preferences_notification_period_30_minutes
                RefreshPeriodUi.OneHour -> R.string.preferences_notification_period_1_hour
                RefreshPeriodUi.TwoHours -> R.string.preferences_notification_period_2_hours
                RefreshPeriodUi.FourHours -> R.string.preferences_notification_period_4_hours
                RefreshPeriodUi.EightHours -> R.string.preferences_notification_period_8_hours
            }
                .let { resources.getString(it) }
        }
    }

    private fun openAppearanceSettings() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, AppearancePreferencesFragment())
            .addToBackStack(null)
            .commit()
    }
}
