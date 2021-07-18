package io.github.wykopmobilny.domain.settings.di

import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.domain.settings.GetAppearancePreferencesQuery
import io.github.wykopmobilny.domain.settings.GetGeneralPreferencesQuery
import io.github.wykopmobilny.ui.settings.GetAppearancePreferences
import io.github.wykopmobilny.ui.settings.GetGeneralPreferences

@Module
internal abstract class SettingsModule {

    @Binds
    abstract fun GetGeneralPreferencesQuery.generalPreferences(): GetGeneralPreferences

    @Binds
    abstract fun GetAppearancePreferencesQuery.appearancePreferences(): GetAppearancePreferences
}
