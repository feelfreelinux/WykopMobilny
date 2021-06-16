package io.github.wykopmobilny.styles

import io.github.wykopmobilny.ui.base.Query

interface GetAppStyle : Query<StyleUi>

data class StyleUi(
    val theme: AppThemeUi,
)

enum class AppThemeUi {
    Light,
    Dark,
    DarkAmoled,
}
