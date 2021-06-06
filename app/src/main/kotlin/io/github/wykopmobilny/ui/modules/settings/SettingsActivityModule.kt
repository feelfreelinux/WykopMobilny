package io.github.wykopmobilny.ui.modules.settings

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi

@Module
class SettingsActivityModule {
    @Provides
    fun provideNavigator(settingsActivity: SettingsActivity): NewNavigatorApi = NewNavigator(settingsActivity)
}
