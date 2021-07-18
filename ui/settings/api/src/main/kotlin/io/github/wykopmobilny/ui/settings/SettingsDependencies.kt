package io.github.wykopmobilny.ui.settings

interface SettingsDependencies {

    fun general(): GetGeneralPreferences

    fun appearance(): GetAppearancePreferences
}
