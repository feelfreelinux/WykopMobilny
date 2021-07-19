package io.github.wykopmobilny.domain.settings

import io.github.wykopmobilny.domain.navigation.InteropRequestsProvider
import io.github.wykopmobilny.domain.navigation.InteropRequest
import io.github.wykopmobilny.domain.navigation.InteropRequest.ClearSuggestionDatabase
import io.github.wykopmobilny.domain.settings.di.SettingsScope
import io.github.wykopmobilny.domain.settings.prefs.GetFilteringPreferences
import io.github.wykopmobilny.domain.settings.prefs.GetNotificationPreferences
import io.github.wykopmobilny.domain.settings.prefs.NotificationsPreferences.RefreshPeriod
import io.github.wykopmobilny.storage.api.UserInfoStorage
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import io.github.wykopmobilny.ui.base.AppScopes
import io.github.wykopmobilny.ui.base.launchIn
import io.github.wykopmobilny.ui.settings.FilteringUi
import io.github.wykopmobilny.ui.settings.GeneralPreferencesUi
import io.github.wykopmobilny.ui.settings.GeneralPreferencesUi.NotificationsUi.RefreshPeriodUi
import io.github.wykopmobilny.ui.settings.GetGeneralPreferences
import io.github.wykopmobilny.ui.settings.ListSetting
import io.github.wykopmobilny.ui.settings.Setting
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGeneralPreferencesQuery @Inject internal constructor(
    private val getNotificationPreferences: GetNotificationPreferences,
    private val getFilteringPreferences: GetFilteringPreferences,
    private val userPreferences: UserPreferenceApi,
    private val userInfoStorage: UserInfoStorage,
    private val interopRequests: InteropRequestsProvider,
    private val appScopes: AppScopes,
) : GetGeneralPreferences {

    override fun invoke() =
        combine(
            notificationsFlow(),
            filteringFlow(),
        ) { notifications, filtering ->
            GeneralPreferencesUi(
                notifications = notifications,
                filtering = filtering,
            )
        }

    private fun notificationsFlow() =
        getNotificationPreferences()
            .map { notifications ->
                GeneralPreferencesUi.NotificationsUi(
                    notificationsEnabled = Setting(
                        currentValue = notifications.notificationsEnabled,
                        onClicked = { updateUserSetting(UserSettings.notificationsEnabled, !notifications.notificationsEnabled) },
                    ),
                    notificationRefreshPeriod = ListSetting(
                        values = RefreshPeriodUi.values().toList(),
                        currentValue = notifications.notificationRefreshPeriod.toUi(),
                        isEnabled = notifications.notificationsEnabled,
                        onSelected = { updateUserSetting(UserSettings.notificationsRefreshPeriod, it.toDomain().duration) },
                    ),
                    exitConfirmation = Setting(
                        currentValue = notifications.exitConfirmation,
                        onClicked = { updateUserSetting(UserSettings.exitConfirmation, !notifications.exitConfirmation) },
                    ),
                )
            }

    private fun filteringFlow() =
        combine(
            getFilteringPreferences(),
            userInfoStorage.loggedUser,
        ) { filtering, loggedUser ->
            val manageBlackList: (() -> Unit)? = if (loggedUser == null) {
                null
            } else {
                { appScopes.launchIn<SettingsScope> { interopRequests.request(InteropRequest.BlackListScreen) } }
            }
            FilteringUi(
                showPlus18Content = Setting(
                    currentValue = !filtering.hidePlus18Content,
                    onClicked = { updateUserSetting(UserSettings.hidePlus18Content, !filtering.hidePlus18Content) },
                ),
                showNsfwContent = Setting(
                    currentValue = !filtering.hideNsfwContent,
                    onClicked = { updateUserSetting(UserSettings.hideNsfwContent, !filtering.hideNsfwContent) },
                ),
                hideNewUserContent = Setting(
                    currentValue = filtering.hideNewUserContent,
                    onClicked = { updateUserSetting(UserSettings.hideNewUserContent, !filtering.hideNewUserContent) },
                ),
                hideContentWithNoTags = Setting(
                    currentValue = filtering.hideContentWithNoTags,
                    onClicked = { updateUserSetting(UserSettings.hideContentWithNoTags, !filtering.hideContentWithNoTags) },
                ),
                hideBlacklistedContent = Setting(
                    currentValue = filtering.hideBlacklistedContent,
                    onClicked = { updateUserSetting(UserSettings.hideBlacklistedContent, !filtering.hideBlacklistedContent) },
                ),
                manageBlackList = manageBlackList,
                useEmbeddedBrowser = Setting(
                    currentValue = filtering.useEmbeddedBrowser,
                    onClicked = { updateUserSetting(UserSettings.useEmbeddedBrowser, !filtering.useEmbeddedBrowser) },
                ),
                clearSearchHistory = { appScopes.launchIn<SettingsScope> { interopRequests.request(ClearSuggestionDatabase) } },
            )
        }

    private fun <T> updateUserSetting(key: UserSetting<T>, value: T) {
        appScopes.launchIn<SettingsScope> { userPreferences.update(key, value) }
    }
}

private fun RefreshPeriod.toUi() = when (this) {
    RefreshPeriod.FifteenMinutes -> RefreshPeriodUi.FifteenMinutes
    RefreshPeriod.ThirtyMinutes -> RefreshPeriodUi.ThirtyMinutes
    RefreshPeriod.OneHour -> RefreshPeriodUi.OneHour
    RefreshPeriod.TwoHours -> RefreshPeriodUi.TwoHours
    RefreshPeriod.FourHours -> RefreshPeriodUi.FourHours
    RefreshPeriod.EightHours -> RefreshPeriodUi.EightHours
}

private fun RefreshPeriodUi.toDomain() = when (this) {
    RefreshPeriodUi.FifteenMinutes -> RefreshPeriod.FifteenMinutes
    RefreshPeriodUi.ThirtyMinutes -> RefreshPeriod.ThirtyMinutes
    RefreshPeriodUi.OneHour -> RefreshPeriod.OneHour
    RefreshPeriodUi.TwoHours -> RefreshPeriod.TwoHours
    RefreshPeriodUi.FourHours -> RefreshPeriod.FourHours
    RefreshPeriodUi.EightHours -> RefreshPeriod.EightHours
}
