package io.github.wykopmobilny.domain.styles

import io.github.wykopmobilny.domain.navigation.NightModeDetector
import io.github.wykopmobilny.domain.navigation.NightModeState
import io.github.wykopmobilny.storage.api.AppTheme
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.styles.AppThemeUi
import io.github.wykopmobilny.styles.GetAppStyle
import io.github.wykopmobilny.styles.StyleUi
import io.github.wykopmobilny.ui.base.AppScopes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

internal class GetAppStyleQuery @Inject constructor(
    private val appAppearanceStore: SettingsPreferencesApi,
    private val nightModeDetector: NightModeDetector,
) : GetAppStyle {

    override fun invoke(): Flow<StyleUi> =
        appAppearanceStore.theme.map { theme ->
            StyleUi(
                theme = theme?.toUi() ?: findDefaultTheme(),
            )
        }
            .distinctUntilChanged()
            .shareIn(AppScopes.applicationScope, SharingStarted.Eagerly, replay = 1)

    private suspend fun findDefaultTheme(): AppThemeUi =
        when (nightModeDetector.getNightModeState()) {
            NightModeState.Enabled -> AppThemeUi.Dark
            NightModeState.Disabled -> AppThemeUi.Light
            NightModeState.Unknown -> AppThemeUi.Light
        }
}

private fun AppTheme.toUi() = when (this) {
    AppTheme.Light -> AppThemeUi.Light
    AppTheme.Dark -> AppThemeUi.Dark
    AppTheme.DarkAmoled -> AppThemeUi.DarkAmoled
}
