package io.github.wykopmobilny.domain.settings.prefs

import io.github.wykopmobilny.domain.settings.FontSize
import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.domain.styles.AppTheme
import io.github.wykopmobilny.domain.styles.GetAppTheme
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class GetAppearanceSectionPreferences @Inject constructor(
    private val userPreferences: UserPreferenceApi,
    private val getAppTheme: GetAppTheme,
) {

    operator fun invoke() = combine(
        getAppTheme(),
        userPreferences.get(UserSettings.useAmoledTheme),
        userPreferences.get(UserSettings.font),
        userPreferences.get(UserSettings.defaultScreen),
    ) { currentAppTheme, amoledTheme, fontSize, defaultScreen ->
        val isDarkTheme = when (currentAppTheme) {
            AppTheme.Light -> false
            AppTheme.Dark -> true
            AppTheme.DarkAmoled -> true
        }
        val isAmoledTheme = amoledTheme == true
        val screen = defaultScreen ?: MainScreen.Promoted
        val font = fontSize ?: FontSize.Normal

        AppearanceSection(
            isDarkTheme = isDarkTheme,
            isAmoledTheme = isAmoledTheme,
            defaultScreen = screen,
            defaultFont = font,
        )
    }
}

internal data class AppearanceSection(
    val isDarkTheme: Boolean,
    val isAmoledTheme: Boolean,
    val defaultScreen: MainScreen,
    val defaultFont: FontSize,
)

internal enum class MainScreen {
    Promoted,
    Mikroblog,
    MyWykop,
    Hits,
}
