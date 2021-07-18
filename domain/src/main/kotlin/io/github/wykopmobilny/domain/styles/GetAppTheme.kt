package io.github.wykopmobilny.domain.styles

import io.github.wykopmobilny.domain.navigation.NightModeDetector
import io.github.wykopmobilny.domain.navigation.NightModeState
import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

internal class GetAppTheme @Inject constructor(
    private val userPreferenceApi: UserPreferenceApi,
    private val nightModeDetector: NightModeDetector,
) {

    operator fun invoke() =
        combine(
            userPreferenceApi.get(UserSettings.darkTheme),
            userPreferenceApi.get(UserSettings.useAmoledTheme),
        ) { darkTheme, amoledTheme ->
            if (darkTheme == null) {
                findDefaultAppTheme()
            } else {
                if (darkTheme == true) {
                    if (amoledTheme == true) {
                        AppTheme.DarkAmoled
                    } else {
                        AppTheme.Dark
                    }
                } else {
                    AppTheme.Light
                }
            }
        }
            .distinctUntilChanged()

    private suspend fun findDefaultAppTheme() =
        when (nightModeDetector.getNightModeState()) {
            NightModeState.Enabled -> AppTheme.Dark
            NightModeState.Disabled -> AppTheme.Light
            NightModeState.Unknown -> AppTheme.Light
        }
}
