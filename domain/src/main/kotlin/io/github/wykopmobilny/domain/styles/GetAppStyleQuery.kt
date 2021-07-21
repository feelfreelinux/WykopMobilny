package io.github.wykopmobilny.domain.styles

import io.github.wykopmobilny.styles.AppThemeUi
import io.github.wykopmobilny.styles.GetAppStyle
import io.github.wykopmobilny.styles.StyleUi
import io.github.wykopmobilny.ui.base.AppScopes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

internal class GetAppStyleQuery @Inject constructor(
    private val getAppTheme: GetAppTheme,
    private val appScopes: AppScopes,
) : GetAppStyle {

    override fun invoke() =
        getAppTheme()
            .map { theme ->
                StyleUi(
                    theme = theme.toUi(),
                )
            }
            .distinctUntilChanged()
            .shareIn(appScopes.applicationScope, SharingStarted.Eagerly, replay = 1)
}

private fun AppTheme.toUi() = when (this) {
    AppTheme.Light -> AppThemeUi.Light
    AppTheme.Dark -> AppThemeUi.Dark
    AppTheme.DarkAmoled -> AppThemeUi.DarkAmoled
}
